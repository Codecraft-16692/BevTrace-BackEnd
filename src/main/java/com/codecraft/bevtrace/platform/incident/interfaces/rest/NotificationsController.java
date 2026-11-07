package com.codecraft.bevtrace.platform.incident.interfaces.rest;

import com.codecraft.bevtrace.platform.incident.application.queryservices.NotificationQueryService;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetNotificationByIdQuery;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetAllNotificationsQuery;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.NotificationResource;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
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
import com.codecraft.bevtrace.platform.incident.application.commandservices.NotificationCommandService;
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
 * Endpoints of the notifications resource.
 */
@RestController
@RequestMapping(value = "/api/v1/notifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Notifications of the incident context")
public class NotificationsController {

    private final NotificationQueryService queryService;
    private final NotificationCommandService commandService;

    public NotificationsController(NotificationQueryService queryService, NotificationCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List notifications",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<NotificationResource>> getAll(@RequestParam MultiValueMap<String, String> params,
                                                             Authentication authentication) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        if (!isAdmin(authentication)) {
            filters.put("recipientRole", authentication.getAuthorities().stream().map(a -> a.getAuthority()).toList());
        }
        var query = new GetAllNotificationsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(NotificationResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a notification by id")
    public ResponseEntity<?> getById(@PathVariable Long id, Authentication authentication) {
        return queryService.handle(new GetNotificationByIdQuery(id))
                .filter(n -> isAdmin(authentication) || authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals(n.getRecipientRole())))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(NotificationResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("Notification", id)));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<?> markRead(@PathVariable Long id) {
        var result = commandService.handle(IncidentCommandFromResourceAssembler.toMarkReadCommand(id));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                NotificationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    private static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
