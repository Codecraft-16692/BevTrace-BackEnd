package com.codecraft.bevtrace.platform.inventory.domain.repositories;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.ProductBatch;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored product batchs.
 */
public interface ProductBatchRepository {

    Optional<ProductBatch> findById(Long id);

    boolean existsById(Long id);

    ProductBatch save(ProductBatch productBatch);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<ProductBatch> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    Optional<ProductBatch> findByBatchNumber(String batchNumber);
    boolean existsByBatchNumber(String batchNumber);
}
