package com.codecraft.bevtrace.platform.telemetry.domain.repositories;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.LocationStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored location streams.
 */
public interface LocationStreamRepository {

    Optional<LocationStream> findById(Long id);

    boolean existsById(Long id);

    LocationStream save(LocationStream locationStream);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<LocationStream> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    Optional<LocationStream> findFirstByDeviceIdOrderByTimestampDesc(Long deviceId);
}
