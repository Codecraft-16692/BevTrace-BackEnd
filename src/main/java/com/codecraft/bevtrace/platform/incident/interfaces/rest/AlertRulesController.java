package com.codecraft.bevtrace.platform.incident.interfaces.rest;

import com.codecraft.bevtrace.platform.incident.application.queryservices.AlertRuleQueryService;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetAlertRuleByIdQuery;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetAllAlertRulesQuery;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.AlertRuleResource;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.transform.AlertRuleResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.transform.IncidentCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.incident.application.commandservices.AlertRuleCommandService;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.CreateAlertRuleResource;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.UpdateAlertRuleStatusResource;
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
 * Endpoints of the alert rules resource.
 */
@RestController
@RequestMapping(value = "/api/v1/rules", produces = APPLICATION_JSON_VALUE)
@Tag(name = "AlertRules", description = "Alert Rules of the incident context")
public class AlertRulesController {

    private final AlertRuleQueryService queryService;
    private final AlertRuleCommandService commandService;

    public AlertRulesController(AlertRuleQueryService queryService, AlertRuleCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List alert rules",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<AlertRuleResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllAlertRulesQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(AlertRuleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a alert rule by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetAlertRuleByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(AlertRuleResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("AlertRule", id)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOGISTICS_MANAGER')")
    @PostMapping
    @Operation(summary = "Create an alert rule")
    public ResponseEntity<?> create(@Valid @RequestBody CreateAlertRuleResource resource) {
        var result = commandService.handle(IncidentCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                AlertRuleResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOGISTICS_MANAGER')")
    @PatchMapping("/{id}/active")
    @Operation(summary = "Enable or disable a rule")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @Valid @RequestBody UpdateAlertRuleStatusResource resource) {
        var result = commandService.handle(IncidentCommandFromResourceAssembler.toCommandFromResource(id, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                AlertRuleResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
