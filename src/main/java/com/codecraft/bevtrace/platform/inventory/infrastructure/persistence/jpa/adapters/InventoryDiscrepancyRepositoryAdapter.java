package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryDiscrepancy;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.InventoryDiscrepancyRepository;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.InventoryDiscrepancyPersistenceEntity;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories.InventoryDiscrepancyJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.DiscrepancyStatus;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists InventoryDiscrepancy through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class InventoryDiscrepancyRepositoryAdapter implements InventoryDiscrepancyRepository {

    private final InventoryDiscrepancyJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public InventoryDiscrepancyRepositoryAdapter(InventoryDiscrepancyJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<InventoryDiscrepancy> findById(Long id) {
        return jpa.findById(id).map(InventoryDiscrepancyPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public InventoryDiscrepancy save(InventoryDiscrepancy aggregate) {
        var saved = jpa.save(InventoryDiscrepancyPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<InventoryDiscrepancy> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<InventoryDiscrepancyPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(InventoryDiscrepancyPersistenceEntity::toDomain).toList();
    }

    @Override
    public long countByReconciliationIdAndStatus(Long reconciliationId, DiscrepancyStatus status) {
        return jpa.countByReconciliationIdAndStatus(reconciliationId, status);
    }
}
