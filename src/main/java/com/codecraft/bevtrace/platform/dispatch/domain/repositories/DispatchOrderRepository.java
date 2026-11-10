package com.codecraft.bevtrace.platform.dispatch.domain.repositories;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DispatchOrder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored dispatch orders.
 */
public interface DispatchOrderRepository {

    Optional<DispatchOrder> findById(Long id);

    boolean existsById(Long id);

    DispatchOrder save(DispatchOrder dispatchOrder);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<DispatchOrder> search(Map<String, List<String>> filters, String sort, String order, Integer limit);
}
