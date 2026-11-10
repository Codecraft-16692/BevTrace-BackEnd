package com.codecraft.bevtrace.platform.subscription.interfaces.rest;

import com.codecraft.bevtrace.platform.iam.interfaces.acl.IamContextFacade;
import com.codecraft.bevtrace.platform.subscription.application.commandservices.SubscriptionCommandService;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.SubscribeResource;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform.SubscriptionCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.codecraft.bevtrace.platform.subscription.application.queryservices.SubscriptionQueryService;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetSubscriptionByIdQuery;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetAllSubscriptionsQuery;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.SubscriptionResource;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
 * Endpoints of the subscriptions resource.
 */
@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "Subscriptions of the subscription context")
public class SubscriptionsController {

    private final SubscriptionQueryService queryService;
    private final IamContextFacade iam;
    private final SubscriptionCommandService commandService;

    public SubscriptionsController(SubscriptionQueryService queryService, IamContextFacade iam,
                                   SubscriptionCommandService commandService) {
        this.queryService = queryService;
        this.iam = iam;
        this.commandService = commandService;
    }

    private Long currentUserId(Authentication authentication) {
        return iam.fetchUserIdByEmail(authentication.getName()).orElse(-1L);
    }

    private static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List subscriptions",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<SubscriptionResource>> getAll(@RequestParam MultiValueMap<String, String> params,
                                                 Authentication authentication) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        if (!isAdmin(authentication)) {
            filters.put("userId", List.of(String.valueOf(currentUserId(authentication))));
        }
        var query = new GetAllSubscriptionsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a subscription by id")
    public ResponseEntity<?> getById(@PathVariable Long id, Authentication authentication) {
        return queryService.handle(new GetSubscriptionByIdQuery(id))
                .filter(e -> isAdmin(authentication) || currentUserId(authentication).equals(e.getUserId()))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(SubscriptionResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("Subscription", id)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @Operation(summary = "Buy a plan",
            description = "Simulated gateway. Card 4000000000000002 is always declined (HTTP 422, details subscription.errors.payment-declined).")
    public ResponseEntity<?> subscribe(@Valid @RequestBody SubscribeResource resource, Authentication authentication) {
        var command = SubscriptionCommandFromResourceAssembler.toCommandFromResource(currentUserId(authentication), resource);
        return ResponseEntityAssembler.toResponseEntityFromResult(commandService.handle(command),
                SubscriptionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/cancellation")
    @Operation(summary = "Cancel a subscription")
    public ResponseEntity<?> cancel(@PathVariable Long id, Authentication authentication) {
        var command = SubscriptionCommandFromResourceAssembler.toCancelCommand(
                currentUserId(authentication), isAdmin(authentication), id);
        return ResponseEntityAssembler.toResponseEntityFromResult(commandService.handle(command),
                SubscriptionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
