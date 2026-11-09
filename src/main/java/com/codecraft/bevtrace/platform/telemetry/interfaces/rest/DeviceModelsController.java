package com.codecraft.bevtrace.platform.telemetry.interfaces.rest;

import com.codecraft.bevtrace.platform.telemetry.application.queryservices.DeviceModelQueryService;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetDeviceModelByIdQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetAllDeviceModelsQuery;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources.DeviceModelResource;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.transform.DeviceModelResourceFromEntityAssembler;
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
 * Endpoints of the device models resource.
 */
@RestController
@RequestMapping(value = "/api/v1/models", produces = APPLICATION_JSON_VALUE)
@Tag(name = "DeviceModels", description = "Device Models of the telemetry context")
public class DeviceModelsController {

    private final DeviceModelQueryService queryService;

    public DeviceModelsController(DeviceModelQueryService queryService) {
        this.queryService = queryService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List device models",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<DeviceModelResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllDeviceModelsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(DeviceModelResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a device model by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetDeviceModelByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(DeviceModelResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("DeviceModel", id)));
    }
}
