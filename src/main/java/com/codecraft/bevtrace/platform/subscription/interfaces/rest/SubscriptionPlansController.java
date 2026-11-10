package com.codecraft.bevtrace.platform.subscription.interfaces.rest;

import com.codecraft.bevtrace.platform.subscription.application.queryservices.SubscriptionPlanQueryService;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetSubscriptionPlanByIdQuery;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetAllSubscriptionPlansQuery;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.SubscriptionPlanResource;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform.SubscriptionPlanResourceFromEntityAssembler;
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
 * Endpoints of the subscription plans resource.
 */
@RestController
@RequestMapping(value = "/api/v1/subscription-plans", produces = APPLICATION_JSON_VALUE)
@Tag(name = "SubscriptionPlans", description = "Subscription Plans of the subscription context")
public class SubscriptionPlansController {

    private final SubscriptionPlanQueryService queryService;

    public SubscriptionPlansController(SubscriptionPlanQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    @Operation(summary = "List subscription plans",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<SubscriptionPlanResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllSubscriptionPlansQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(SubscriptionPlanResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a subscription plan by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetSubscriptionPlanByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(SubscriptionPlanResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("SubscriptionPlan", id)));
    }
}
