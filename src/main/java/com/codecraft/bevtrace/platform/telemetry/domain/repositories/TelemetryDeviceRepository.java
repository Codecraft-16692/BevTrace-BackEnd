package com.codecraft.bevtrace.platform.telemetry.domain.repositories;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.TelemetryDevice;
import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.ConnectionStatus;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored telemetry devices.
 */
public interface TelemetryDeviceRepository {

    Optional<TelemetryDevice> findById(Long id);

    boolean existsById(Long id);

    TelemetryDevice save(TelemetryDevice telemetryDevice);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<TelemetryDevice> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    boolean existsByDeviceCode(String deviceCode);
    boolean existsByVehicleId(Long vehicleId);
    Optional<TelemetryDevice> findByVehicleId(Long vehicleId);
    List<TelemetryDevice> findSilent(ConnectionStatus status, Instant before);
}
