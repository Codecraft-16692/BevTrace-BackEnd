package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.ProductBatch;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductBatchRepository;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.ProductBatchPersistenceEntity;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories.ProductBatchJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists ProductBatch through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class ProductBatchRepositoryAdapter implements ProductBatchRepository {

    private final ProductBatchJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public ProductBatchRepositoryAdapter(ProductBatchJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<ProductBatch> findById(Long id) {
        return jpa.findById(id).map(ProductBatchPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public ProductBatch save(ProductBatch aggregate) {
        var saved = jpa.save(ProductBatchPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<ProductBatch> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<ProductBatchPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(ProductBatchPersistenceEntity::toDomain).toList();
    }

    @Override
    public Optional<ProductBatch> findByBatchNumber(String batchNumber) {
        return jpa.findByBatchNumber(batchNumber).map(ProductBatchPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsByBatchNumber(String batchNumber) {
        return jpa.existsByBatchNumber(batchNumber);
    }
}
