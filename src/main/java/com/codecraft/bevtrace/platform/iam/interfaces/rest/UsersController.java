package com.codecraft.bevtrace.platform.iam.interfaces.rest;

import com.codecraft.bevtrace.platform.iam.application.commandservices.UserCommandService;
import com.codecraft.bevtrace.platform.iam.application.queryservices.UserQueryService;
import com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.UpdateUserRoleResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.UpdateUserStatusResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.transform.UserCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @Operation(summary = "List users",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<UserResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllUsersQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a user by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetUserByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("User", id)));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/role")
    @Operation(summary = "Assign a role to a user")
    public ResponseEntity<?> assignRole(@AuthenticationPrincipal UserDetailsImpl actor, @PathVariable Long id,
                                        @Valid @RequestBody UpdateUserRoleResource resource) {
        var result = commandService.handle(UserCommandFromResourceAssembler.toCommandFromResource(actor.getId(), id, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/active")
    @Operation(summary = "Enable or disable a user")
    public ResponseEntity<?> changeStatus(@AuthenticationPrincipal UserDetailsImpl actor, @PathVariable Long id,
                                          @Valid @RequestBody UpdateUserStatusResource resource) {
        var result = commandService.handle(UserCommandFromResourceAssembler.toCommandFromResource(actor.getId(), id, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
