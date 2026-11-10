package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.CorrectiveAction;
import com.codecraft.bevtrace.platform.incident.domain.repositories.CorrectiveActionRepository;
import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities.CorrectiveActionPersistenceEntity;
import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.repositories.CorrectiveActionJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists CorrectiveAction through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class CorrectiveActionRepositoryAdapter implements CorrectiveActionRepository {

    private final CorrectiveActionJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public CorrectiveActionRepositoryAdapter(CorrectiveActionJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<CorrectiveAction> findById(Long id) {
        return jpa.findById(id).map(CorrectiveActionPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public CorrectiveAction save(CorrectiveAction aggregate) {
        var saved = jpa.save(CorrectiveActionPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<CorrectiveAction> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<CorrectiveActionPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(CorrectiveActionPersistenceEntity::toDomain).toList();
    }

    @Override
    public long countByIncidentId(Long incidentId) {
        return jpa.countByIncidentId(incidentId);
    }
}
