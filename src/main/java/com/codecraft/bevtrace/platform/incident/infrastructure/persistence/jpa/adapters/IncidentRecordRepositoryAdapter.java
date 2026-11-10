package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.IncidentRecord;
import com.codecraft.bevtrace.platform.incident.domain.repositories.IncidentRecordRepository;
import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities.IncidentRecordPersistenceEntity;
import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.repositories.IncidentRecordJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists IncidentRecord through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class IncidentRecordRepositoryAdapter implements IncidentRecordRepository {

    private final IncidentRecordJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public IncidentRecordRepositoryAdapter(IncidentRecordJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<IncidentRecord> findById(Long id) {
        return jpa.findById(id).map(IncidentRecordPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public IncidentRecord save(IncidentRecord aggregate) {
        var saved = jpa.save(IncidentRecordPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<IncidentRecord> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<IncidentRecordPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(IncidentRecordPersistenceEntity::toDomain).toList();
    }

    @Override
    public List<IncidentRecord> findByLogIdAndRuleId(Long logId, Long ruleId) {
        return jpa.findByLogIdAndRuleId(logId, ruleId).stream().map(IncidentRecordPersistenceEntity::toDomain).toList();
    }
}
