package com.codecraft.bevtrace.platform.telemetry.interfaces.rest;

import com.codecraft.bevtrace.platform.telemetry.application.queryservices.TelemetryDeviceQueryService;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetTelemetryDeviceByIdQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetAllTelemetryDevicesQuery;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources.TelemetryDeviceResource;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.transform.TelemetryDeviceResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.transform.TelemetryCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.telemetry.application.commandservices.TelemetryDeviceCommandService;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources.ProvisionDeviceResource;
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
 * Endpoints of the telemetry devices resource.
 */
@RestController
@RequestMapping(value = "/api/v1/devices", produces = APPLICATION_JSON_VALUE)
@Tag(name = "TelemetryDevices", description = "Telemetry Devices of the telemetry context")
public class TelemetryDevicesController {

    private final TelemetryDeviceQueryService queryService;
    private final TelemetryDeviceCommandService commandService;

    public TelemetryDevicesController(TelemetryDeviceQueryService queryService, TelemetryDeviceCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List telemetry devices",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<TelemetryDeviceResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllTelemetryDevicesQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(TelemetryDeviceResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a telemetry device by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetTelemetryDeviceByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(TelemetryDeviceResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("TelemetryDevice", id)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOGISTICS_MANAGER')")
    @PostMapping
    @Operation(summary = "Provision a device in a vehicle")
    public ResponseEntity<?> provision(@Valid @RequestBody ProvisionDeviceResource resource) {
        var result = commandService.handle(TelemetryCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                TelemetryDeviceResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }
}
