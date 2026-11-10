package com.codecraft.bevtrace.platform.incident.interfaces.rest;

import com.codecraft.bevtrace.platform.incident.application.queryservices.IncidentRecordQueryService;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetIncidentRecordByIdQuery;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetAllIncidentRecordsQuery;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.IncidentRecordResource;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.transform.IncidentRecordResourceFromEntityAssembler;
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
import com.codecraft.bevtrace.platform.incident.application.commandservices.IncidentRecordCommandService;
import com.codecraft.bevtrace.platform.incident.application.commandservices.CorrectiveActionCommandService;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.RegisterCorrectiveActionResource;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.DetectionResource;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.transform.CorrectiveActionResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.iam.interfaces.acl.IamContextFacade;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
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
 * Endpoints of the incident records resource.
 */
@RestController
@RequestMapping(value = "/api/v1/incidents", produces = APPLICATION_JSON_VALUE)
@Tag(name = "IncidentRecords", description = "Incident Records of the incident context")
public class IncidentRecordsController {

    private final IncidentRecordQueryService queryService;
    private final IncidentRecordCommandService commandService;
    private final CorrectiveActionCommandService actionCommandService;
    private final IamContextFacade iam;

    public IncidentRecordsController(IncidentRecordQueryService queryService, IncidentRecordCommandService commandService,
                                     CorrectiveActionCommandService actionCommandService, IamContextFacade iam) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.actionCommandService = actionCommandService;
        this.iam = iam;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List incident records",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<IncidentRecordResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllIncidentRecordsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(IncidentRecordResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a incident record by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetIncidentRecordByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(IncidentRecordResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("IncidentRecord", id)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_LOGISTICS_MANAGER')")
    @PostMapping("/detections")
    @Operation(summary = "Run the anomaly detection now",
            description = "Evaluates the active rules on every shipment in transit.")
    public ResponseEntity<?> detect() {
        var result = commandService.handle(IncidentCommandFromResourceAssembler.toDetectCommand());
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                d -> new DetectionResource(d.created(), d.reopened()), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/acknowledgement")
    @Operation(summary = "Acknowledge an open incident")
    public ResponseEntity<?> acknowledge(@PathVariable Long id, Authentication authentication) {
        var userId = iam.fetchUserIdByEmail(authentication.getName()).orElseThrow();
        var result = commandService.handle(IncidentCommandFromResourceAssembler.toAcknowledgeCommand(id, userId));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                IncidentRecordResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/corrective-actions")
    @Operation(summary = "Register a corrective action")
    public ResponseEntity<?> registerAction(@PathVariable Long id, Authentication authentication,
                                            @Valid @RequestBody RegisterCorrectiveActionResource resource) {
        var userId = iam.fetchUserIdByEmail(authentication.getName()).orElseThrow();
        var result = actionCommandService.handle(
                IncidentCommandFromResourceAssembler.toCommandFromResource(id, userId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                CorrectiveActionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/resolution")
    @Operation(summary = "Resolve an incident", description = "Needs at least one corrective action.")
    public ResponseEntity<?> resolve(@PathVariable Long id) {
        var result = commandService.handle(IncidentCommandFromResourceAssembler.toResolveCommand(id));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                IncidentRecordResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
