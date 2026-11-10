package com.codecraft.bevtrace.platform.incident.domain.repositories;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.IncidentRecord;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored incident records.
 */
public interface IncidentRecordRepository {

    Optional<IncidentRecord> findById(Long id);

    boolean existsById(Long id);

    IncidentRecord save(IncidentRecord incidentRecord);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<IncidentRecord> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    List<IncidentRecord> findByLogIdAndRuleId(Long logId, Long ruleId);
}
