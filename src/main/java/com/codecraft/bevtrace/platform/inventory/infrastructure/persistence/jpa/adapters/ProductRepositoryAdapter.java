package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.Product;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductRepository;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.ProductPersistenceEntity;
import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories.ProductJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists Product through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public ProductRepositoryAdapter(ProductJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpa.findById(id).map(ProductPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public Product save(Product aggregate) {
        var saved = jpa.save(ProductPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<Product> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<ProductPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(ProductPersistenceEntity::toDomain).toList();
    }
}
