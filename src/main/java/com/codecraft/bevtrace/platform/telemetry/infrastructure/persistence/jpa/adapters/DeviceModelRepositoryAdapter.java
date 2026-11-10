package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.DeviceModel;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.DeviceModelRepository;
import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities.DeviceModelPersistenceEntity;
import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.repositories.DeviceModelJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists DeviceModel through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class DeviceModelRepositoryAdapter implements DeviceModelRepository {

    private final DeviceModelJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public DeviceModelRepositoryAdapter(DeviceModelJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<DeviceModel> findById(Long id) {
        return jpa.findById(id).map(DeviceModelPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public DeviceModel save(DeviceModel aggregate) {
        var saved = jpa.save(DeviceModelPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<DeviceModel> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<DeviceModelPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(DeviceModelPersistenceEntity::toDomain).toList();
    }
}
