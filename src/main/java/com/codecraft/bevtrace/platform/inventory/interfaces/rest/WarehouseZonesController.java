package com.codecraft.bevtrace.platform.inventory.interfaces.rest;

import com.codecraft.bevtrace.platform.inventory.application.queryservices.WarehouseZoneQueryService;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetWarehouseZoneByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllWarehouseZonesQuery;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.WarehouseZoneResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform.WarehouseZoneResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
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
 * Endpoints of the warehouse zones resource.
 */
@RestController
@RequestMapping(value = "/api/v1/zones", produces = APPLICATION_JSON_VALUE)
@Tag(name = "WarehouseZones", description = "Warehouse Zones of the inventory context")
public class WarehouseZonesController {

    private final WarehouseZoneQueryService queryService;

    public WarehouseZonesController(WarehouseZoneQueryService queryService) {
        this.queryService = queryService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List warehouse zones",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<WarehouseZoneResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllWarehouseZonesQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(WarehouseZoneResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a warehouse zone by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetWarehouseZoneByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(WarehouseZoneResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("WarehouseZone", id)));
    }
}
