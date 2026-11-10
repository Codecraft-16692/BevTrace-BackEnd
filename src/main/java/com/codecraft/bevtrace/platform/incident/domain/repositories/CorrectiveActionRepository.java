package com.codecraft.bevtrace.platform.incident.domain.repositories;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.CorrectiveAction;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored corrective actions.
 */
public interface CorrectiveActionRepository {

    Optional<CorrectiveAction> findById(Long id);

    boolean existsById(Long id);

    CorrectiveAction save(CorrectiveAction correctiveAction);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<CorrectiveAction> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    long countByIncidentId(Long incidentId);
}
