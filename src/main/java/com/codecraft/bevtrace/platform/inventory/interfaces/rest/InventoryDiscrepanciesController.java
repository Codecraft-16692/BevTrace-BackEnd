package com.codecraft.bevtrace.platform.inventory.interfaces.rest;

import com.codecraft.bevtrace.platform.inventory.application.queryservices.InventoryDiscrepancyQueryService;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetInventoryDiscrepancyByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllInventoryDiscrepanciesQuery;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.InventoryDiscrepancyResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform.InventoryDiscrepancyResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.codecraft.bevtrace.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.web.bind.annotation.PathVariable;
import com.codecraft.bevtrace.platform.inventory.application.commandservices.InventoryDiscrepancyCommandService;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.ResolveDiscrepancyResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform.InventoryDiscrepancyCommandFromResourceAssembler;
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
 * Endpoints of the inventory discrepancies resource.
 */
@RestController
@RequestMapping(value = "/api/v1/discrepancies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "InventoryDiscrepancies", description = "Inventory Discrepancies of the inventory context")
public class InventoryDiscrepanciesController {

    private final InventoryDiscrepancyQueryService queryService;
    private final InventoryDiscrepancyCommandService commandService;

    public InventoryDiscrepanciesController(InventoryDiscrepancyQueryService queryService, InventoryDiscrepancyCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List inventory discrepancies",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<InventoryDiscrepancyResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllInventoryDiscrepanciesQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(InventoryDiscrepancyResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a inventory discrepancy by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetInventoryDiscrepancyByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(InventoryDiscrepancyResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("InventoryDiscrepancy", id)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LOGISTICS_MANAGER')")
    @PostMapping("/{id}/resolution")
    @Operation(summary = "Resolve a discrepancy", description = "Adjusts the batch stock to the counted units.")
    public ResponseEntity<?> resolve(@PathVariable Long id, @Valid @RequestBody ResolveDiscrepancyResource resource) {
        var result = commandService.handle(
                InventoryDiscrepancyCommandFromResourceAssembler.toCommandFromResource(id, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                InventoryDiscrepancyResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
