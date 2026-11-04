package com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.Product;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.ProductResource;

/**
 * Builds the resource of a product.
 */
public final class ProductResourceFromEntityAssembler {

    private ProductResourceFromEntityAssembler() {
    }

    public static ProductResource toResourceFromEntity(Product e) {
        return new ProductResource(e.getId(), e.getSkuCode(), e.getName(), e.getCategory(), e.getVolumeMl(), e.getPackagingType());
    }
}
