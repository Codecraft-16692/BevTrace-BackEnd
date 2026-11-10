package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.LocationStream;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.LocationStreamRepository;
import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities.LocationStreamPersistenceEntity;
import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.repositories.LocationStreamJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists LocationStream through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class LocationStreamRepositoryAdapter implements LocationStreamRepository {

    private final LocationStreamJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public LocationStreamRepositoryAdapter(LocationStreamJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<LocationStream> findById(Long id) {
        return jpa.findById(id).map(LocationStreamPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public LocationStream save(LocationStream aggregate) {
        var saved = jpa.save(LocationStreamPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<LocationStream> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<LocationStreamPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(LocationStreamPersistenceEntity::toDomain).toList();
    }

    @Override
    public Optional<LocationStream> findFirstByDeviceIdOrderByTimestampDesc(Long deviceId) {
        return jpa.findFirstByDeviceIdOrderByTimestampDesc(deviceId).map(LocationStreamPersistenceEntity::toDomain);
    }
}
