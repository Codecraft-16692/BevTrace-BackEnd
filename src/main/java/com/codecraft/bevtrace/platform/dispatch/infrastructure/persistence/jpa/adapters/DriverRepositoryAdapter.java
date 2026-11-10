package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.Driver;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.DriverRepository;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.DriverPersistenceEntity;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.repositories.DriverJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists Driver through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class DriverRepositoryAdapter implements DriverRepository {

    private final DriverJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public DriverRepositoryAdapter(DriverJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<Driver> findById(Long id) {
        return jpa.findById(id).map(DriverPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public Driver save(Driver aggregate) {
        var saved = jpa.save(DriverPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<Driver> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<DriverPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(DriverPersistenceEntity::toDomain).toList();
    }
}
