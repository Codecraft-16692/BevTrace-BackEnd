package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.Notification;
import com.codecraft.bevtrace.platform.incident.domain.repositories.NotificationRepository;
import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities.NotificationPersistenceEntity;
import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.repositories.NotificationJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists Notification through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final NotificationJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public NotificationRepositoryAdapter(NotificationJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return jpa.findById(id).map(NotificationPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public Notification save(Notification aggregate) {
        var saved = jpa.save(NotificationPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<Notification> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<NotificationPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(NotificationPersistenceEntity::toDomain).toList();
    }
}
