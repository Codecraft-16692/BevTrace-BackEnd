package com.codecraft.bevtrace.platform.dispatch.domain.repositories;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.Driver;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored drivers.
 */
public interface DriverRepository {

    Optional<Driver> findById(Long id);

    boolean existsById(Long id);

    Driver save(Driver driver);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<Driver> search(Map<String, List<String>> filters, String sort, String order, Integer limit);
}
