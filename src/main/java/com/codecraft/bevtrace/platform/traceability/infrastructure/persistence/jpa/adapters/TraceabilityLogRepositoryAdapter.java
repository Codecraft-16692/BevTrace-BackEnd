package com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.TraceabilityLog;
import com.codecraft.bevtrace.platform.traceability.domain.repositories.TraceabilityLogRepository;
import com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.entities.TraceabilityLogPersistenceEntity;
import com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.repositories.TraceabilityLogJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.LogStatus;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists TraceabilityLog through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class TraceabilityLogRepositoryAdapter implements TraceabilityLogRepository {

    private final TraceabilityLogJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public TraceabilityLogRepositoryAdapter(TraceabilityLogJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<TraceabilityLog> findById(Long id) {
        return jpa.findById(id).map(TraceabilityLogPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public TraceabilityLog save(TraceabilityLog aggregate) {
        var saved = jpa.save(TraceabilityLogPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<TraceabilityLog> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<TraceabilityLogPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(TraceabilityLogPersistenceEntity::toDomain).toList();
    }

    @Override
    public Optional<TraceabilityLog> findByDispatchOrderId(Long dispatchOrderId) {
        return jpa.findByDispatchOrderId(dispatchOrderId).map(TraceabilityLogPersistenceEntity::toDomain);
    }

    @Override
    public List<TraceabilityLog> findByCurrentStatus(LogStatus status) {
        return jpa.findByCurrentStatus(status).stream().map(TraceabilityLogPersistenceEntity::toDomain).toList();
    }

    @Override
    public Optional<TraceabilityLog> findFirstByDeviceIdAndCurrentStatus(Long deviceId, LogStatus status) {
        return jpa.findFirstByDeviceIdAndCurrentStatus(deviceId, status).map(TraceabilityLogPersistenceEntity::toDomain);
    }
}
