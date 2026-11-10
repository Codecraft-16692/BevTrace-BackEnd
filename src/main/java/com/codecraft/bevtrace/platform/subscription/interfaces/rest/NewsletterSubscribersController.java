package com.codecraft.bevtrace.platform.subscription.interfaces.rest;

import com.codecraft.bevtrace.platform.subscription.application.commandservices.NewsletterSubscriberCommandService;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.NewsletterSubscriptionResource;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform.SubscriptionCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.codecraft.bevtrace.platform.subscription.application.queryservices.NewsletterSubscriberQueryService;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetNewsletterSubscriberByIdQuery;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetAllNewsletterSubscribersQuery;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.NewsletterSubscriberResource;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform.NewsletterSubscriberResourceFromEntityAssembler;
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
 * Endpoints of the newsletter subscribers resource.
 */
@RestController
@RequestMapping(value = "/api/v1/newsletter-subscribers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "NewsletterSubscribers", description = "Newsletter Subscribers of the subscription context")
public class NewsletterSubscribersController {

    private final NewsletterSubscriberQueryService queryService;
    private final NewsletterSubscriberCommandService commandService;

    public NewsletterSubscribersController(NewsletterSubscriberQueryService queryService, NewsletterSubscriberCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @Operation(summary = "List newsletter subscribers",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<NewsletterSubscriberResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllNewsletterSubscribersQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(NewsletterSubscriberResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a newsletter subscriber by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetNewsletterSubscriberByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(NewsletterSubscriberResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("NewsletterSubscriber", id)));
    }

    @PostMapping
    @Operation(summary = "Public form: newsletter")
    public ResponseEntity<?> create(@Valid @RequestBody NewsletterSubscriptionResource resource) {
        var result = commandService.handle(SubscriptionCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                NewsletterSubscriberResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }
}
