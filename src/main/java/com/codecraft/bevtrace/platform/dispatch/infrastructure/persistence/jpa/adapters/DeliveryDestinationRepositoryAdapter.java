package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DeliveryDestination;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.DeliveryDestinationRepository;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.DeliveryDestinationPersistenceEntity;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.repositories.DeliveryDestinationJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists DeliveryDestination through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class DeliveryDestinationRepositoryAdapter implements DeliveryDestinationRepository {

    private final DeliveryDestinationJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public DeliveryDestinationRepositoryAdapter(DeliveryDestinationJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<DeliveryDestination> findById(Long id) {
        return jpa.findById(id).map(DeliveryDestinationPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public DeliveryDestination save(DeliveryDestination aggregate) {
        var saved = jpa.save(DeliveryDestinationPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<DeliveryDestination> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<DeliveryDestinationPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(DeliveryDestinationPersistenceEntity::toDomain).toList();
    }
}
