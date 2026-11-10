package com.codecraft.bevtrace.platform.subscription.interfaces.rest;

import com.codecraft.bevtrace.platform.subscription.application.commandservices.ContactRequestCommandService;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.SubmitContactRequestResource;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform.SubscriptionCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.codecraft.bevtrace.platform.subscription.application.queryservices.ContactRequestQueryService;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetContactRequestByIdQuery;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetAllContactRequestsQuery;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.ContactRequestResource;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform.ContactRequestResourceFromEntityAssembler;
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
 * Endpoints of the contact requests resource.
 */
@RestController
@RequestMapping(value = "/api/v1/contact-requests", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ContactRequests", description = "Contact Requests of the subscription context")
public class ContactRequestsController {

    private final ContactRequestQueryService queryService;
    private final ContactRequestCommandService commandService;

    public ContactRequestsController(ContactRequestQueryService queryService, ContactRequestCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @Operation(summary = "List contact requests",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<ContactRequestResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllContactRequestsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(ContactRequestResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a contact request by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetContactRequestByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(ContactRequestResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("ContactRequest", id)));
    }

    @PostMapping
    @Operation(summary = "Public form: contact request")
    public ResponseEntity<?> create(@Valid @RequestBody SubmitContactRequestResource resource) {
        var result = commandService.handle(SubscriptionCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                ContactRequestResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }
}
