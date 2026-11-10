package com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.RouteCheckpoint;
import com.codecraft.bevtrace.platform.traceability.domain.repositories.RouteCheckpointRepository;
import com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.entities.RouteCheckpointPersistenceEntity;
import com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.repositories.RouteCheckpointJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists RouteCheckpoint through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class RouteCheckpointRepositoryAdapter implements RouteCheckpointRepository {

    private final RouteCheckpointJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public RouteCheckpointRepositoryAdapter(RouteCheckpointJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<RouteCheckpoint> findById(Long id) {
        return jpa.findById(id).map(RouteCheckpointPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public RouteCheckpoint save(RouteCheckpoint aggregate) {
        var saved = jpa.save(RouteCheckpointPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<RouteCheckpoint> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<RouteCheckpointPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(RouteCheckpointPersistenceEntity::toDomain).toList();
    }

    @Override
    public Optional<RouteCheckpoint> findFirstByLogIdOrderBySequenceDesc(Long logId) {
        return jpa.findFirstByLogIdOrderBySequenceDesc(logId).map(RouteCheckpointPersistenceEntity::toDomain);
    }
}
