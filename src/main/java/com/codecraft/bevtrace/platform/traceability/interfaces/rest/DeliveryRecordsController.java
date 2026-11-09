package com.codecraft.bevtrace.platform.traceability.interfaces.rest;

import com.codecraft.bevtrace.platform.traceability.application.queryservices.DeliveryRecordQueryService;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetDeliveryRecordByIdQuery;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetAllDeliveryRecordsQuery;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources.DeliveryRecordResource;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.transform.DeliveryRecordResourceFromEntityAssembler;
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
 * Endpoints of the delivery records resource.
 */
@RestController
@RequestMapping(value = "/api/v1/delivery-records", produces = APPLICATION_JSON_VALUE)
@Tag(name = "DeliveryRecords", description = "Delivery Records of the traceability context")
public class DeliveryRecordsController {

    private final DeliveryRecordQueryService queryService;

    public DeliveryRecordsController(DeliveryRecordQueryService queryService) {
        this.queryService = queryService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List delivery records",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<DeliveryRecordResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllDeliveryRecordsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(DeliveryRecordResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a delivery record by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetDeliveryRecordByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(DeliveryRecordResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("DeliveryRecord", id)));
    }
}
