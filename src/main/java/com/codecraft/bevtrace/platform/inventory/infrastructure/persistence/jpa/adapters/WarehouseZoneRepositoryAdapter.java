package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.WarehouseZone;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.WarehouseZoneRepository;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.WarehouseZonePersistenceEntity;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories.WarehouseZoneJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists WarehouseZone through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class WarehouseZoneRepositoryAdapter implements WarehouseZoneRepository {

    private final WarehouseZoneJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public WarehouseZoneRepositoryAdapter(WarehouseZoneJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<WarehouseZone> findById(Long id) {
        return jpa.findById(id).map(WarehouseZonePersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public WarehouseZone save(WarehouseZone aggregate) {
        var saved = jpa.save(WarehouseZonePersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<WarehouseZone> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<WarehouseZonePersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(WarehouseZonePersistenceEntity::toDomain).toList();
    }
}
