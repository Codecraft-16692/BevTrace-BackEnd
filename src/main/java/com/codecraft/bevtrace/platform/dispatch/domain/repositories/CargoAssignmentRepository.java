package com.codecraft.bevtrace.platform.dispatch.domain.repositories;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.CargoAssignment;
import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchStatus;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored cargo assignments.
 */
public interface CargoAssignmentRepository {

    Optional<CargoAssignment> findById(Long id);

    boolean existsById(Long id);

    CargoAssignment save(CargoAssignment cargoAssignment);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<CargoAssignment> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    List<CargoAssignment> findByDispatchOrderId(Long dispatchOrderId);

    /**
     * @param batchId  batch
     * @param statuses order states that still hold stock
     * @return units of the batch reserved by orders in those states
     */
    long reservedUnits(Long batchId, Collection<DispatchStatus> statuses);
}
