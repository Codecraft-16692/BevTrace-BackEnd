package com.codecraft.bevtrace.platform.traceability.interfaces.rest;

import com.codecraft.bevtrace.platform.traceability.application.queryservices.RouteCheckpointQueryService;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetRouteCheckpointByIdQuery;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetAllRouteCheckpointsQuery;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources.RouteCheckpointResource;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.transform.RouteCheckpointResourceFromEntityAssembler;
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
 * Endpoints of the route checkpoints resource.
 */
@RestController
@RequestMapping(value = "/api/v1/route-checkpoints", produces = APPLICATION_JSON_VALUE)
@Tag(name = "RouteCheckpoints", description = "Route Checkpoints of the traceability context")
public class RouteCheckpointsController {

    private final RouteCheckpointQueryService queryService;

    public RouteCheckpointsController(RouteCheckpointQueryService queryService) {
        this.queryService = queryService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List route checkpoints",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<RouteCheckpointResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllRouteCheckpointsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(RouteCheckpointResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a route checkpoint by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetRouteCheckpointByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(RouteCheckpointResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("RouteCheckpoint", id)));
    }
}
