package com.codecraft.bevtrace.platform.traceability.domain.repositories;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.TraceabilityLog;
import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.LogStatus;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored traceability logs.
 */
public interface TraceabilityLogRepository {

    Optional<TraceabilityLog> findById(Long id);

    boolean existsById(Long id);

    TraceabilityLog save(TraceabilityLog traceabilityLog);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<TraceabilityLog> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    Optional<TraceabilityLog> findByDispatchOrderId(Long dispatchOrderId);
    List<TraceabilityLog> findByCurrentStatus(LogStatus status);
    Optional<TraceabilityLog> findFirstByDeviceIdAndCurrentStatus(Long deviceId, LogStatus status);
}
