package com.codecraft.bevtrace.platform.telemetry.interfaces.rest;

import com.codecraft.bevtrace.platform.telemetry.application.queryservices.DisconnectionPeriodQueryService;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetDisconnectionPeriodByIdQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetAllDisconnectionPeriodsQuery;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources.DisconnectionPeriodResource;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.transform.DisconnectionPeriodResourceFromEntityAssembler;
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
 * Endpoints of the disconnection periods resource.
 */
@RestController
@RequestMapping(value = "/api/v1/disconnection-periods", produces = APPLICATION_JSON_VALUE)
@Tag(name = "DisconnectionPeriods", description = "Disconnection Periods of the telemetry context")
public class DisconnectionPeriodsController {

    private final DisconnectionPeriodQueryService queryService;

    public DisconnectionPeriodsController(DisconnectionPeriodQueryService queryService) {
        this.queryService = queryService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List disconnection periods",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<DisconnectionPeriodResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllDisconnectionPeriodsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(DisconnectionPeriodResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a disconnection period by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetDisconnectionPeriodByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(DisconnectionPeriodResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("DisconnectionPeriod", id)));
    }
}
