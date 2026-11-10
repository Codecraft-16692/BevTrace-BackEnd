package com.codecraft.bevtrace.platform.dispatch.interfaces.rest;

import com.codecraft.bevtrace.platform.dispatch.application.queryservices.DriverQueryService;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetDriverByIdQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetAllDriversQuery;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.DriverResource;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.transform.DriverResourceFromEntityAssembler;
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
 * Endpoints of the drivers resource.
 */
@RestController
@RequestMapping(value = "/api/v1/drivers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Drivers", description = "Drivers of the dispatch context")
public class DriversController {

    private final DriverQueryService queryService;

    public DriversController(DriverQueryService queryService) {
        this.queryService = queryService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List drivers",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<DriverResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllDriversQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(DriverResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a driver by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetDriverByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(DriverResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("Driver", id)));
    }
}
