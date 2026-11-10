package com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.DeliveryRecord;
import com.codecraft.bevtrace.platform.traceability.domain.repositories.DeliveryRecordRepository;
import com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.entities.DeliveryRecordPersistenceEntity;
import com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.repositories.DeliveryRecordJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists DeliveryRecord through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class DeliveryRecordRepositoryAdapter implements DeliveryRecordRepository {

    private final DeliveryRecordJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public DeliveryRecordRepositoryAdapter(DeliveryRecordJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<DeliveryRecord> findById(Long id) {
        return jpa.findById(id).map(DeliveryRecordPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public DeliveryRecord save(DeliveryRecord aggregate) {
        var saved = jpa.save(DeliveryRecordPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<DeliveryRecord> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<DeliveryRecordPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(DeliveryRecordPersistenceEntity::toDomain).toList();
    }
}
