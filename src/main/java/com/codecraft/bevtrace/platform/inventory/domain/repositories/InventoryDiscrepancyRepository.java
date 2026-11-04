package com.codecraft.bevtrace.platform.inventory.domain.repositories;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryDiscrepancy;
import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.DiscrepancyStatus;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored inventory discrepancys.
 */
public interface InventoryDiscrepancyRepository {

    Optional<InventoryDiscrepancy> findById(Long id);

    boolean existsById(Long id);

    InventoryDiscrepancy save(InventoryDiscrepancy inventoryDiscrepancy);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<InventoryDiscrepancy> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    long countByReconciliationIdAndStatus(Long reconciliationId, DiscrepancyStatus status);
}
