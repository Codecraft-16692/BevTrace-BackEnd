package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Subscription;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.SubscriptionRepository;
import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.repositories.SubscriptionJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists Subscription through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final SubscriptionJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public SubscriptionRepositoryAdapter(SubscriptionJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return jpa.findById(id).map(SubscriptionPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public Subscription save(Subscription aggregate) {
        var saved = jpa.save(SubscriptionPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<Subscription> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<SubscriptionPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(SubscriptionPersistenceEntity::toDomain).toList();
    }

    @Override
    public boolean existsByUserIdAndStatusIn(Long userId, Collection<SubscriptionStatus> statuses) {
        return jpa.existsByUserIdAndStatusIn(userId, statuses);
    }
}
