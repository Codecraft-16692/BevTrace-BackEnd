package com.codecraft.bevtrace.platform.inventory.interfaces.rest;

import com.codecraft.bevtrace.platform.inventory.application.queryservices.WasteRecordQueryService;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetWasteRecordByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllWasteRecordsQuery;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.WasteRecordResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform.WasteRecordResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.codecraft.bevtrace.platform.iam.interfaces.acl.IamContextFacade;
import com.codecraft.bevtrace.platform.inventory.application.commandservices.WasteRecordCommandService;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.ReportWasteResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform.WasteRecordCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Endpoints of the waste records resource.
 */
@RestController
@RequestMapping(value = "/api/v1/waste-records", produces = APPLICATION_JSON_VALUE)
@Tag(name = "WasteRecords", description = "Waste Records of the inventory context")
public class WasteRecordsController {

    private final WasteRecordQueryService queryService;
    private final WasteRecordCommandService commandService;
    private final IamContextFacade iam;

    public WasteRecordsController(WasteRecordQueryService queryService, WasteRecordCommandService commandService, IamContextFacade iam) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.iam = iam;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List waste records",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<WasteRecordResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllWasteRecordsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(WasteRecordResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a waste record by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetWasteRecordByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(WasteRecordResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("WasteRecord", id)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @Operation(summary = "Report waste", description = "Deducts the quantity from the batch stock.")
    public ResponseEntity<?> reportWaste(Authentication authentication, @Valid @RequestBody ReportWasteResource resource) {
        var userId = iam.fetchUserIdByEmail(authentication.getName()).orElseThrow();
        var result = commandService.handle(WasteRecordCommandFromResourceAssembler.toCommandFromResource(userId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                WasteRecordResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }
}
