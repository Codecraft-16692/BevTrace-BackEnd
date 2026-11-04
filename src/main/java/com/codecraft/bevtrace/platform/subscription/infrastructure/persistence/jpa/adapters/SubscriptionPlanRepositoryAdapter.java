package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities.SubscriptionPlanPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.repositories.SubscriptionPlanJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists SubscriptionPlan through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class SubscriptionPlanRepositoryAdapter implements SubscriptionPlanRepository {

    private final SubscriptionPlanJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public SubscriptionPlanRepositoryAdapter(SubscriptionPlanJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<SubscriptionPlan> findById(Long id) {
        return jpa.findById(id).map(SubscriptionPlanPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public SubscriptionPlan save(SubscriptionPlan aggregate) {
        var saved = jpa.save(SubscriptionPlanPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<SubscriptionPlan> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<SubscriptionPlanPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(SubscriptionPlanPersistenceEntity::toDomain).toList();
    }

    @Override
    public Optional<SubscriptionPlan> findByCodeAndBillingPeriod(String code, BillingPeriod billingPeriod) {
        return jpa.findByCodeAndBillingPeriod(code, billingPeriod).map(SubscriptionPlanPersistenceEntity::toDomain);
    }
}
