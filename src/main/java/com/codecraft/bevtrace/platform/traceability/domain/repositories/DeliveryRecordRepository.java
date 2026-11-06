package com.codecraft.bevtrace.platform.traceability.domain.repositories;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.DeliveryRecord;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored delivery records.
 */
public interface DeliveryRecordRepository {

    Optional<DeliveryRecord> findById(Long id);

    boolean existsById(Long id);

    DeliveryRecord save(DeliveryRecord deliveryRecord);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<DeliveryRecord> search(Map<String, List<String>> filters, String sort, String order, Integer limit);
}
