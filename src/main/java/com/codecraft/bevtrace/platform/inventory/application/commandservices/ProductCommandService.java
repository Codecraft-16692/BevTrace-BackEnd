package com.codecraft.bevtrace.platform.inventory.application.commandservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.Product;
import com.codecraft.bevtrace.platform.inventory.domain.model.commands.RegisterProductCommand;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductRepository;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Commands on the product catalogue: the logistics manager adds the products by hand.
 */
@Service
@Transactional
public class ProductCommandService {

    private final ProductRepository products;

    public ProductCommandService(ProductRepository products) {
        this.products = products;
    }

    public Result<Product, ApplicationError> handle(RegisterProductCommand command) {
        if (products.existsBySkuCode(command.skuCode().trim().toUpperCase())) {
            return Result.failure(ApplicationError.conflict("Product", "inventory.errors.sku-duplicated"));
        }
        return Results.attempt(() -> products.save(Product.register(command.skuCode(), command.name(),
                command.category(), command.volumeMl(), command.packagingType())));
    }
}
