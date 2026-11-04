package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.NewsletterSubscriber;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.NewsletterSubscriberRepository;
import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities.NewsletterSubscriberPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.repositories.NewsletterSubscriberJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists NewsletterSubscriber through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class NewsletterSubscriberRepositoryAdapter implements NewsletterSubscriberRepository {

    private final NewsletterSubscriberJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public NewsletterSubscriberRepositoryAdapter(NewsletterSubscriberJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<NewsletterSubscriber> findById(Long id) {
        return jpa.findById(id).map(NewsletterSubscriberPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public NewsletterSubscriber save(NewsletterSubscriber aggregate) {
        var saved = jpa.save(NewsletterSubscriberPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<NewsletterSubscriber> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<NewsletterSubscriberPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(NewsletterSubscriberPersistenceEntity::toDomain).toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpa.existsByEmail(email);
    }
}
