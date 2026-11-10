package com.codecraft.bevtrace.platform.dispatch.interfaces.rest;

import com.codecraft.bevtrace.platform.dispatch.application.commandservices.DispatchOrderCommandService;
import com.codecraft.bevtrace.platform.dispatch.application.queryservices.DispatchOrderQueryService;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.AssignVehicleResource;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.ChangeDispatchPriorityResource;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.ScheduleDispatchOrderResource;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.ValidateCargoResource;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.transform.DispatchOrderCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.iam.interfaces.acl.IamContextFacade;
import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DispatchOrder;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetDispatchOrderByIdQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetAllDispatchOrdersQuery;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.DispatchOrderResource;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.transform.DispatchOrderResourceFromEntityAssembler;
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
 * Endpoints of the dispatch orders resource.
 */
@RestController
@RequestMapping(value = "/api/v1/dispatch-orders", produces = APPLICATION_JSON_VALUE)
@Tag(name = "DispatchOrders", description = "Dispatch Orders of the dispatch context")
public class DispatchOrdersController {

    private static final String MANAGER = "hasAnyAuthority('ROLE_ADMIN','ROLE_LOGISTICS_MANAGER')";
    private static final String OPERATOR = "hasAnyAuthority('ROLE_ADMIN','ROLE_WAREHOUSE_OPERATOR')";

    private final DispatchOrderQueryService queryService;
    private final DispatchOrderCommandService commandService;
    private final IamContextFacade iam;

    public DispatchOrdersController(DispatchOrderQueryService queryService, DispatchOrderCommandService commandService,
                                    IamContextFacade iam) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.iam = iam;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List dispatch orders",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<DispatchOrderResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllDispatchOrdersQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(DispatchOrderResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a dispatch order by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetDispatchOrderByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(DispatchOrderResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("DispatchOrder", id)));
    }

    @PreAuthorize(MANAGER)
    @PostMapping
    @Operation(summary = "Schedule a dispatch", description = "Checks the free stock of every batch.")
    public ResponseEntity<?> schedule(Authentication authentication, @Valid @RequestBody ScheduleDispatchOrderResource resource) {
        var managerId = iam.fetchUserIdByEmail(authentication.getName()).orElseThrow();
        var result = commandService.handle(DispatchOrderCommandFromResourceAssembler.toCommandFromResource(managerId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                DispatchOrderResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PreAuthorize(MANAGER)
    @PostMapping("/{id}/vehicle-assignment")
    @Operation(summary = "Assign a vehicle", description = "SCHEDULED -> AUTHORIZED; reserves the vehicle.")
    public ResponseEntity<?> assignVehicle(@PathVariable Long id, @Valid @RequestBody AssignVehicleResource resource) {
        var result = commandService.handle(DispatchOrderCommandFromResourceAssembler.toCommandFromResource(id, resource));
        return ok(result);
    }

    @PreAuthorize(OPERATOR)
    @PostMapping("/{id}/cargo-validation")
    @Operation(summary = "Validate the scanned pallets")
    public ResponseEntity<?> validateCargo(@PathVariable Long id, @Valid @RequestBody ValidateCargoResource resource) {
        return ok(commandService.handle(DispatchOrderCommandFromResourceAssembler.toCommandFromResource(id, resource)));
    }

    @PreAuthorize(MANAGER)
    @PatchMapping("/{id}/priority")
    @Operation(summary = "Change the priority of a pending dispatch")
    public ResponseEntity<?> changePriority(@PathVariable Long id, @Valid @RequestBody ChangeDispatchPriorityResource resource) {
        return ok(commandService.handle(DispatchOrderCommandFromResourceAssembler.toCommandFromResource(id, resource)));
    }

    @PreAuthorize(MANAGER)
    @PostMapping("/{id}/departure")
    @Operation(summary = "Register the departure",
            description = "AUTHORIZED -> IN_TRANSIT. Deducts the stock and starts the route traceability.")
    public ResponseEntity<?> depart(@PathVariable Long id) {
        return ok(commandService.handle(DispatchOrderCommandFromResourceAssembler.toDepartureCommand(id)));
    }

    @PreAuthorize(MANAGER)
    @PostMapping("/{id}/cancellation")
    @Operation(summary = "Cancel a pending dispatch")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        return ok(commandService.handle(DispatchOrderCommandFromResourceAssembler.toCancelCommand(id)));
    }

    private static ResponseEntity<?> ok(Result<DispatchOrder, ApplicationError> result) {
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                DispatchOrderResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
