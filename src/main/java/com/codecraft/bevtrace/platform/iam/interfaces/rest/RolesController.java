package com.codecraft.bevtrace.platform.iam.interfaces.rest;

import com.codecraft.bevtrace.platform.iam.application.queryservices.RoleQueryService;
import com.codecraft.bevtrace.platform.iam.domain.model.queries.GetRoleByIdQuery;
import com.codecraft.bevtrace.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.RoleResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.transform.RoleResourceFromEntityAssembler;
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
 * Endpoints of the roles resource.
 */
@RestController
@RequestMapping(value = "/api/v1/roles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Roles", description = "Roles of the iam context")
public class RolesController {

    private final RoleQueryService queryService;

    public RolesController(RoleQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    @Operation(summary = "List roles",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<RoleResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllRolesQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(RoleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a role by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetRoleByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(RoleResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("Role", id)));
    }
}
