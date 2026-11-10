package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.ContactRequest;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.ContactRequestRepository;
import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities.ContactRequestPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.repositories.ContactRequestJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists ContactRequest through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class ContactRequestRepositoryAdapter implements ContactRequestRepository {

    private final ContactRequestJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public ContactRequestRepositoryAdapter(ContactRequestJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<ContactRequest> findById(Long id) {
        return jpa.findById(id).map(ContactRequestPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public ContactRequest save(ContactRequest aggregate) {
        var saved = jpa.save(ContactRequestPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<ContactRequest> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<ContactRequestPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(ContactRequestPersistenceEntity::toDomain).toList();
    }
}
