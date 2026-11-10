package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.DisconnectionPeriod;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.DisconnectionPeriodRepository;
import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities.DisconnectionPeriodPersistenceEntity;
import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.repositories.DisconnectionPeriodJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists DisconnectionPeriod through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class DisconnectionPeriodRepositoryAdapter implements DisconnectionPeriodRepository {

    private final DisconnectionPeriodJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public DisconnectionPeriodRepositoryAdapter(DisconnectionPeriodJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<DisconnectionPeriod> findById(Long id) {
        return jpa.findById(id).map(DisconnectionPeriodPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public DisconnectionPeriod save(DisconnectionPeriod aggregate) {
        var saved = jpa.save(DisconnectionPeriodPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<DisconnectionPeriod> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<DisconnectionPeriodPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(DisconnectionPeriodPersistenceEntity::toDomain).toList();
    }

    @Override
    public Optional<DisconnectionPeriod> findFirstByDeviceIdAndEndedAtIsNull(Long deviceId) {
        return jpa.findFirstByDeviceIdAndEndedAtIsNull(deviceId).map(DisconnectionPeriodPersistenceEntity::toDomain);
    }
}
