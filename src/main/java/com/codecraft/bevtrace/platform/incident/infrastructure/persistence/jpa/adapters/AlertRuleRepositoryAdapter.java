package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.AlertRule;
import com.codecraft.bevtrace.platform.incident.domain.repositories.AlertRuleRepository;
import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities.AlertRulePersistenceEntity;
import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.repositories.AlertRuleJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists AlertRule through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class AlertRuleRepositoryAdapter implements AlertRuleRepository {

    private final AlertRuleJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public AlertRuleRepositoryAdapter(AlertRuleJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<AlertRule> findById(Long id) {
        return jpa.findById(id).map(AlertRulePersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public AlertRule save(AlertRule aggregate) {
        var saved = jpa.save(AlertRulePersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<AlertRule> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<AlertRulePersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(AlertRulePersistenceEntity::toDomain).toList();
    }

    @Override
    public List<AlertRule> findByActiveTrue() {
        return jpa.findByActiveTrue().stream().map(AlertRulePersistenceEntity::toDomain).toList();
    }
}
