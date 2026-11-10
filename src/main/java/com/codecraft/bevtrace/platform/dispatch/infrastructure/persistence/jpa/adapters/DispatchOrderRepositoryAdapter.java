package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DispatchOrder;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.DispatchOrderRepository;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.DispatchOrderPersistenceEntity;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.repositories.DispatchOrderJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists DispatchOrder through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class DispatchOrderRepositoryAdapter implements DispatchOrderRepository {

    private final DispatchOrderJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public DispatchOrderRepositoryAdapter(DispatchOrderJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<DispatchOrder> findById(Long id) {
        return jpa.findById(id).map(DispatchOrderPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public DispatchOrder save(DispatchOrder aggregate) {
        var saved = jpa.save(DispatchOrderPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<DispatchOrder> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<DispatchOrderPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(DispatchOrderPersistenceEntity::toDomain).toList();
    }
}
