package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.WasteRecord;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.WasteRecordRepository;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.WasteRecordPersistenceEntity;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories.WasteRecordJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists WasteRecord through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class WasteRecordRepositoryAdapter implements WasteRecordRepository {

    private final WasteRecordJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public WasteRecordRepositoryAdapter(WasteRecordJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<WasteRecord> findById(Long id) {
        return jpa.findById(id).map(WasteRecordPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public WasteRecord save(WasteRecord aggregate) {
        var saved = jpa.save(WasteRecordPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<WasteRecord> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<WasteRecordPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(WasteRecordPersistenceEntity::toDomain).toList();
    }
}
