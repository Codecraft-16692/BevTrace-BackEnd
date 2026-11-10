package com.codecraft.bevtrace.platform.inventory.application.commandservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.ProductBatch;
import com.codecraft.bevtrace.platform.inventory.domain.model.commands.ReceiveBatchCommand;
import com.codecraft.bevtrace.platform.inventory.domain.model.commands.RegisterBatchCommand;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductBatchRepository;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductRepository;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.WarehouseZoneRepository;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Commands on product batches: announcing them and receiving them at the dock.
 */
@Service
@Transactional
public class ProductBatchCommandService {

    private final ProductBatchRepository batches;
    private final ProductRepository products;
    private final WarehouseZoneRepository zones;

    public ProductBatchCommandService(ProductBatchRepository batches, ProductRepository products,
                                      WarehouseZoneRepository zones) {
        this.batches = batches;
        this.products = products;
        this.zones = zones;
    }

    public Result<ProductBatch, ApplicationError> handle(RegisterBatchCommand command) {
        var code = command.batchNumber().trim().toUpperCase();
        if (!products.existsById(command.productId())) {
            return Result.failure(ApplicationError.notFound("Product", command.productId()));
        }
        if (!zones.existsById(command.zoneId())) {
            return Result.failure(ApplicationError.notFound("Zone", command.zoneId()));
        }
        if (batches.existsByBatchNumber(code)) {
            return Result.failure(ApplicationError.conflict("ProductBatch", "inventory.errors.batch-duplicated"));
        }
        return Results.attempt(() -> batches.save(ProductBatch.announce(command.productId(), command.zoneId(), code,
                command.initialQty(), command.expirationDate())));
    }

    public Result<ProductBatch, ApplicationError> handle(ReceiveBatchCommand command) {
        var code = command.scanCode().trim().toUpperCase();
        var batch = batches.findByBatchNumber(code);
        if (batch.isEmpty()) {
            return Result.failure(ApplicationError.businessRuleViolation("Unknown batch code " + code,
                    "inventory.errors.invalid-code"));
        }
        return Results.attempt(() -> {
            batch.get().receive(Instant.now());
            return batches.save(batch.get());
        });
    }
}
