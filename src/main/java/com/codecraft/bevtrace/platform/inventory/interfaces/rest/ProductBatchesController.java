package com.codecraft.bevtrace.platform.inventory.interfaces.rest;

import com.codecraft.bevtrace.platform.inventory.application.queryservices.ProductBatchQueryService;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetProductBatchByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllProductBatchesQuery;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.ProductBatchResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform.ProductBatchResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.codecraft.bevtrace.platform.iam.interfaces.acl.IamContextFacade;
import com.codecraft.bevtrace.platform.inventory.application.commandservices.ProductBatchCommandService;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.RegisterBatchResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.ReceiveBatchResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform.ProductBatchCommandFromResourceAssembler;
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
 * Endpoints of the product batches resource.
 */
@RestController
@RequestMapping(value = "/api/v1/batches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ProductBatches", description = "Product Batches of the inventory context")
public class ProductBatchesController {

    private final ProductBatchQueryService queryService;
    private final ProductBatchCommandService commandService;

    public ProductBatchesController(ProductBatchQueryService queryService, ProductBatchCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "List product batches",
            description = "Filter with ?field=value (repeat for IN), sort with _sort and _order, cap with _limit.")
    public ResponseEntity<List<ProductBatchResource>> getAll(@RequestParam MultiValueMap<String, String> params) {
        var filters = new HashMap<String, List<String>>();
        params.forEach(filters::put);
        var query = new GetAllProductBatchesQuery(filters, params.getFirst("_sort"), params.getFirst("_order"),
                params.getFirst("_limit") == null ? null : Integer.valueOf(params.getFirst("_limit")));
        var resources = queryService.handle(query).stream()
                .map(ProductBatchResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a product batch by id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryService.handle(new GetProductBatchByIdQuery(id))
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(ProductBatchResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElseGet(() -> ErrorResponseAssembler.toErrorResponseFromApplicationError(
                        ApplicationError.notFound("ProductBatch", id)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @Operation(summary = "Register a batch announced by the plant")
    public ResponseEntity<?> registerBatch(@Valid @RequestBody RegisterBatchResource resource) {
        var result = commandService.handle(ProductBatchCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                ProductBatchResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/receptions")
    @Operation(summary = "Receive a batch by scanning its code",
            description = "Moves the batch from EXPECTED to AVAILABLE with its full quantity.")
    public ResponseEntity<?> receive(@Valid @RequestBody ReceiveBatchResource resource) {
        var result = commandService.handle(ProductBatchCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                ProductBatchResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
