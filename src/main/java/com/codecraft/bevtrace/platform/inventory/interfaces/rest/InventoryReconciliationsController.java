package com.codecraft.bevtrace.platform.inventory.interfaces.rest;

import com.codecraft.bevtrace.platform.inventory.application.queryservices.InventoryReconciliationQueryService;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetInventoryReconciliationByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllInventoryReconciliationsQuery;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.InventoryReconciliationResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform.InventoryReconciliationResourceFromEntityAssembler;
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
import com.codecraft.bevtrace.platform.inventory.application.commandservices.InventoryReconciliationCommandService;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.RunReconciliationResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform.InventoryReconciliationCommandFromResourceAssembler;
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
 * Endpoints of the inventory reconciliations resource.
 */
@RestController
@RequestMapping(value = "/api/v1/reconciliations", produces = APPLICATION_JSON_VALUE)
@Tag(name = "InventoryReconciliations", description = "Inventory Reconciliations of the inventory context")
public class InventoryReconciliationsController {

    private final InventoryReconciliationQueryService queryService;
    private final InventoryReconciliationCommandService commandService;
    private final IamContextFacade iam;

    public InventoryReconciliationsController(InventoryReconciliationQueryService queryService, InventoryReconciliationCommandService commandService, IamContextFacade iam) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.iam = iam;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List inventory reconciliations",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<InventoryReconciliationResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllInventoryReconciliationsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(InventoryReconciliationResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a inventory reconciliation by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetInventoryReconciliationByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(InventoryReconciliationResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("InventoryReconciliation", id)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @Operation(summary = "Run a physical reconciliation")
    public ResponseEntity<?> run(Authentication authentication, @Valid @RequestBody RunReconciliationResource resource) {
        var userId = iam.fetchUserIdByEmail(authentication.getName()).orElseThrow();
        var result = commandService.handle(
                InventoryReconciliationCommandFromResourceAssembler.toCommandFromResource(userId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                InventoryReconciliationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LOGISTICS_MANAGER')")
    @PostMapping("/{id}/confirmation")
    @Operation(summary = "Confirm a reconciliation", description = "Fails while discrepancies are open.")
    public ResponseEntity<?> confirm(Authentication authentication, @PathVariable Long id) {
        var userId = iam.fetchUserIdByEmail(authentication.getName()).orElseThrow();
        var result = commandService.handle(
                InventoryReconciliationCommandFromResourceAssembler.toCommandFromResource(id, userId));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                InventoryReconciliationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
