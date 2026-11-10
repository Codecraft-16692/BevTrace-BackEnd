package com.codecraft.bevtrace.platform.subscription.interfaces.rest;

import com.codecraft.bevtrace.platform.iam.interfaces.acl.IamContextFacade;
import com.codecraft.bevtrace.platform.subscription.application.queryservices.PaymentQueryService;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetPaymentByIdQuery;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetAllPaymentsQuery;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.PaymentResource;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
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
 * Endpoints of the payments resource.
 */
@RestController
@RequestMapping(value = "/api/v1/payments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Payments of the subscription context")
public class PaymentsController {

    private final PaymentQueryService queryService;
    private final IamContextFacade iam;

    public PaymentsController(PaymentQueryService queryService, IamContextFacade iam) {
        this.queryService = queryService;
        this.iam = iam;
    }

    private Long currentUserId(Authentication authentication) {
        return iam.fetchUserIdByEmail(authentication.getName()).orElse(-1L);
    }

    private static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List payments",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<PaymentResource>> getAll(@RequestParam MultiValueMap<String, String> params,
                                                 Authentication authentication) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        if (!isAdmin(authentication)) {
            filters.put("userId", List.of(String.valueOf(currentUserId(authentication))));
        }
        var query = new GetAllPaymentsQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a payment by id")
    public ResponseEntity<?> getById(@PathVariable Long id, Authentication authentication) {
        return queryService.handle(new GetPaymentByIdQuery(id))
                .filter(e -> isAdmin(authentication) || currentUserId(authentication).equals(e.getUserId()))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(PaymentResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("Payment", id)));
    }
}
