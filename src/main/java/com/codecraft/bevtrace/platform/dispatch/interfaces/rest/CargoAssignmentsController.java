package com.codecraft.bevtrace.platform.dispatch.interfaces.rest;

import com.codecraft.bevtrace.platform.dispatch.application.queryservices.CargoAssignmentQueryService;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetCargoAssignmentByIdQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetAllCargoAssignmentsQuery;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.CargoAssignmentResource;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.transform.CargoAssignmentResourceFromEntityAssembler;
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
 * Endpoints of the cargo assignments resource.
 */
@RestController
@RequestMapping(value = "/api/v1/cargo-assignments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "CargoAssignments", description = "Cargo Assignments of the dispatch context")
public class CargoAssignmentsController {

    private final CargoAssignmentQueryService queryService;

    public CargoAssignmentsController(CargoAssignmentQueryService queryService) {
        this.queryService = queryService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List cargo assignments",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<CargoAssignmentResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllCargoAssignmentsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(CargoAssignmentResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a cargo assignment by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetCargoAssignmentByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(CargoAssignmentResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("CargoAssignment", id)));
    }
}
