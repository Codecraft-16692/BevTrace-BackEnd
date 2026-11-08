package com.codecraft.bevtrace.platform.analytics.interfaces.rest;

import com.codecraft.bevtrace.platform.analytics.application.queryservices.IndicatorQueryService;
import com.codecraft.bevtrace.platform.analytics.domain.model.queries.GetIndicatorsQuery;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Indicators of a period, computed on the server.
 */
@RestController
@RequestMapping(value = "/api/v1/analytics/indicators", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Indicators", description = "OTIF, fill rate, ERI, inventory rotation and shrinkage")
public class IndicatorsController {

    private final IndicatorQueryService queryService;

    public IndicatorsController(IndicatorQueryService queryService) {
        this.queryService = queryService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LOGISTICS_MANAGER')")
    @GetMapping
    @Operation(summary = "Compute the indicators of a period")
    public ResponseEntity<?> getIndicators(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.handle(new GetIndicatorsQuery(periodStart, periodEnd)), indicators -> indicators, HttpStatus.OK);
    }
}
