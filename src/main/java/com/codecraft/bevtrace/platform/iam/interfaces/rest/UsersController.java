package com.codecraft.bevtrace.platform.iam.interfaces.rest;

import com.codecraft.bevtrace.platform.iam.application.commandservices.UserCommandService;
import com.codecraft.bevtrace.platform.iam.application.queryservices.UserQueryService;
import com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.GrantPermissionResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.transform.UserCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.codecraft.bevtrace.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.codecraft.bevtrace.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.UserResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
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
 * Endpoints of the users resource.
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Users of the iam context")
public class UsersController {

    private final UserQueryService queryService;
    private final UserCommandService commandService;

    public UsersController(UserQueryService queryService, UserCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    private static final String MANAGEMENT = "hasAnyAuthority('ROLE_ADMIN','ROLE_LOGISTICS_MANAGER')";

    private static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @PreAuthorize(MANAGEMENT)
    @GetMapping
    @Operation(summary = "List users",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit. "
                    + "A logistics manager only sees the warehouse operators.")
    public ResponseEntity<List<UserResource>> getAll(@RequestParam MultiValueMap<String, String> params,
                                                     Authentication authentication) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllUsersQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var users = isAdmin(authentication) ? queryService.handle(query) : queryService.handleForManager(query);
        return ResponseEntity.ok(users.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @PreAuthorize(MANAGEMENT)
    @GetMapping("/{id}")
    @Operation(summary = "Get a user by id")
    public ResponseEntity<?> getById(@PathVariable Long id, Authentication authentication) {
        var query = new GetUserByIdQuery(id);
        var user = isAdmin(authentication) ? queryService.handle(query) : queryService.handleForManager(query);
        return user
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("User", id)));
    }

    @PreAuthorize(MANAGEMENT)
    @PostMapping("/{id}/permissions")
    @Operation(summary = "Grant a permission to a user",
            description = "Grants the role and enables the account. The logistics manager can only grant the "
                    + "warehouse operator role; the administrator can grant any role.")
    public ResponseEntity<?> grantPermission(@AuthenticationPrincipal UserDetailsImpl actor, Authentication authentication,
                                             @PathVariable Long id, @Valid @RequestBody GrantPermissionResource resource) {
        var result = commandService.handle(UserCommandFromResourceAssembler.toCommandFromResource(
                actor.getId(), isAdmin(authentication), id, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PreAuthorize(MANAGEMENT)
    @PostMapping("/{id}/revocations")
    @Operation(summary = "Revoke the access of a user",
            description = "Disables the account. A permission has to be granted again to re-enable it.")
    public ResponseEntity<?> revokeAccess(@AuthenticationPrincipal UserDetailsImpl actor, Authentication authentication,
                                          @PathVariable Long id) {
        var result = commandService.handle(UserCommandFromResourceAssembler.toRevokeCommand(
                actor.getId(), isAdmin(authentication), id));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }
}
