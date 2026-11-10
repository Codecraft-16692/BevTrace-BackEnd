package com.codecraft.bevtrace.platform.traceability.interfaces.rest;

import com.codecraft.bevtrace.platform.traceability.application.queryservices.TraceabilityLogQueryService;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetTraceabilityLogByIdQuery;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetAllTraceabilityLogsQuery;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources.TraceabilityLogResource;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.transform.TraceabilityLogResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.codecraft.bevtrace.platform.traceability.application.commandservices.TraceabilityLogCommandService;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources.ConfirmDeliveryResource;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources.RejectDeliveryResource;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.transform.TraceabilityLogCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 * Endpoints of the traceability logs resource.
 */
@RestController
@RequestMapping(value = "/api/v1/logs", produces = APPLICATION_JSON_VALUE)
@Tag(name = "TraceabilityLogs", description = "Traceability Logs of the traceability context")
public class TraceabilityLogsController {

    private final TraceabilityLogQueryService queryService;
    private final TraceabilityLogCommandService commandService;

    public TraceabilityLogsController(TraceabilityLogQueryService queryService,
                                      TraceabilityLogCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List traceability logs",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<TraceabilityLogResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllTraceabilityLogsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(TraceabilityLogResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a traceability log by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetTraceabilityLogByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(TraceabilityLogResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("TraceabilityLog", id)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delivery")
    @Operation(summary = "Confirm the delivery", description = "Completes the log and closes the dispatch order.")
    public ResponseEntity<?> confirmDelivery(@PathVariable Long id, @Valid @RequestBody ConfirmDeliveryResource resource) {
        var result = commandService.handle(TraceabilityLogCommandFromResourceAssembler.toCommandFromResource(id, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                TraceabilityLogResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/rejection")
    @Operation(summary = "Register a rejected delivery", description = "The shipment stays open.")
    public ResponseEntity<?> rejectDelivery(@PathVariable Long id, @Valid @RequestBody RejectDeliveryResource resource) {
        var result = commandService.handle(TraceabilityLogCommandFromResourceAssembler.toCommandFromResource(id, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                TraceabilityLogResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
