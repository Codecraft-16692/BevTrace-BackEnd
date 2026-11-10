package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.TelemetryDevice;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.TelemetryDeviceRepository;
import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities.TelemetryDevicePersistenceEntity;
import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.repositories.TelemetryDeviceJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.ConnectionStatus;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists TelemetryDevice through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class TelemetryDeviceRepositoryAdapter implements TelemetryDeviceRepository {

    private final TelemetryDeviceJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public TelemetryDeviceRepositoryAdapter(TelemetryDeviceJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<TelemetryDevice> findById(Long id) {
        return jpa.findById(id).map(TelemetryDevicePersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public TelemetryDevice save(TelemetryDevice aggregate) {
        var saved = jpa.save(TelemetryDevicePersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<TelemetryDevice> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<TelemetryDevicePersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(TelemetryDevicePersistenceEntity::toDomain).toList();
    }

    @Override
    public boolean existsByDeviceCode(String deviceCode) {
        return jpa.existsByDeviceCode(deviceCode);
    }

    @Override
    public boolean existsByVehicleId(Long vehicleId) {
        return jpa.existsByVehicleId(vehicleId);
    }

    @Override
    public Optional<TelemetryDevice> findByVehicleId(Long vehicleId) {
        return jpa.findByVehicleId(vehicleId).map(TelemetryDevicePersistenceEntity::toDomain);
    }

    @Override
    public List<TelemetryDevice> findSilent(ConnectionStatus status, Instant before) {
        return jpa.findSilent(status, before).stream().map(TelemetryDevicePersistenceEntity::toDomain).toList();
    }
}
