package com.codecraft.bevtrace.platform.analytics.interfaces.rest;

import com.codecraft.bevtrace.platform.analytics.application.commandservices.LogisticsReportCommandService;
import com.codecraft.bevtrace.platform.analytics.interfaces.rest.resources.GenerateLogisticsReportResource;
import com.codecraft.bevtrace.platform.analytics.interfaces.rest.transform.LogisticsReportCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.iam.interfaces.acl.IamContextFacade;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.codecraft.bevtrace.platform.analytics.application.queryservices.LogisticsReportQueryService;
import com.codecraft.bevtrace.platform.analytics.domain.model.queries.GetLogisticsReportByIdQuery;
import com.codecraft.bevtrace.platform.analytics.domain.model.queries.GetAllLogisticsReportsQuery;
import com.codecraft.bevtrace.platform.analytics.interfaces.rest.resources.LogisticsReportResource;
import com.codecraft.bevtrace.platform.analytics.interfaces.rest.transform.LogisticsReportResourceFromEntityAssembler;
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
 * Endpoints of the logistics reports resource.
 */
@RestController
@RequestMapping(value = "/api/v1/logistics-reports", produces = APPLICATION_JSON_VALUE)
@Tag(name = "LogisticsReports", description = "Logistics Reports of the analytics context")
public class LogisticsReportsController {

    private final LogisticsReportQueryService queryService;
    private final LogisticsReportCommandService commandService;
    private final IamContextFacade iam;

    public LogisticsReportsController(LogisticsReportQueryService queryService,
                                      LogisticsReportCommandService commandService, IamContextFacade iam) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.iam = iam;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LOGISTICS_MANAGER')")
    @GetMapping
    @Operation(summary = "List logistics reports",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<LogisticsReportResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllLogisticsReportsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(LogisticsReportResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LOGISTICS_MANAGER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a logistics report by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetLogisticsReportByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(LogisticsReportResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("LogisticsReport", id)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LOGISTICS_MANAGER')")
    @PostMapping
    @Operation(summary = "Generate and save a logistics report")
    public ResponseEntity<?> generate(@Valid @RequestBody GenerateLogisticsReportResource resource,
                                      Authentication authentication) {
        var userId = iam.fetchUserIdByEmail(authentication.getName()).orElse(-1L);
        var command = LogisticsReportCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        return ResponseEntityAssembler.toResponseEntityFromResult(commandService.handle(command),
                LogisticsReportResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }
}
