package com.codecraft.bevtrace.platform.analytics.domain.repositories;

import com.codecraft.bevtrace.platform.analytics.domain.model.aggregates.LogisticsReport;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored logistics reports.
 */
public interface LogisticsReportRepository {

    Optional<LogisticsReport> findById(Long id);

    boolean existsById(Long id);

    LogisticsReport save(LogisticsReport logisticsReport);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<LogisticsReport> search(Map<String, List<String>> filters, String sort, String order, Integer limit);
}
