package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryReconciliation;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.InventoryReconciliationRepository;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.InventoryReconciliationPersistenceEntity;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories.InventoryReconciliationJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists InventoryReconciliation through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class InventoryReconciliationRepositoryAdapter implements InventoryReconciliationRepository {

    private final InventoryReconciliationJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public InventoryReconciliationRepositoryAdapter(InventoryReconciliationJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<InventoryReconciliation> findById(Long id) {
        return jpa.findById(id).map(InventoryReconciliationPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public InventoryReconciliation save(InventoryReconciliation aggregate) {
        var saved = jpa.save(InventoryReconciliationPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<InventoryReconciliation> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<InventoryReconciliationPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(InventoryReconciliationPersistenceEntity::toDomain).toList();
    }
}
