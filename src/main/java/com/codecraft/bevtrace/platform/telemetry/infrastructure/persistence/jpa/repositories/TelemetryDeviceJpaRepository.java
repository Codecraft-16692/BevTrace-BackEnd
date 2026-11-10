package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities.TelemetryDevicePersistenceEntity;
import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.ConnectionStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the TelemetryDevice table; the domain only sees it through {@link TelemetryDeviceRepository}.
 */
@Repository
public interface TelemetryDeviceJpaRepository extends JpaRepository<TelemetryDevicePersistenceEntity, Long>, JpaSpecificationExecutor<TelemetryDevicePersistenceEntity> {

    boolean existsByDeviceCode(String deviceCode);

    boolean existsByVehicleId(Long vehicleId);

    Optional<TelemetryDevicePersistenceEntity> findByVehicleId(Long vehicleId);

    @Query("select d from TelemetryDevicePersistenceEntity d where d.isActive = true and d.connectionStatus = :status and d.lastSignalAt < :before")
    List<TelemetryDevicePersistenceEntity> findSilent(ConnectionStatus status, Instant before);
}
