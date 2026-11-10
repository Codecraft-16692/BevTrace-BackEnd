package com.codecraft.bevtrace.platform.telemetry.domain.repositories;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.DeviceModel;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored device models.
 */
public interface DeviceModelRepository {

    Optional<DeviceModel> findById(Long id);

    boolean existsById(Long id);

    DeviceModel save(DeviceModel deviceModel);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<DeviceModel> search(Map<String, List<String>> filters, String sort, String order, Integer limit);
}
