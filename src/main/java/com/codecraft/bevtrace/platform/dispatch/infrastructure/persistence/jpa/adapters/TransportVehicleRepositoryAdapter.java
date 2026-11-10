package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.TransportVehicle;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.TransportVehicleRepository;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.TransportVehiclePersistenceEntity;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.repositories.TransportVehicleJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists TransportVehicle through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class TransportVehicleRepositoryAdapter implements TransportVehicleRepository {

    private final TransportVehicleJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public TransportVehicleRepositoryAdapter(TransportVehicleJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<TransportVehicle> findById(Long id) {
        return jpa.findById(id).map(TransportVehiclePersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public TransportVehicle save(TransportVehicle aggregate) {
        var saved = jpa.save(TransportVehiclePersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<TransportVehicle> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<TransportVehiclePersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(TransportVehiclePersistenceEntity::toDomain).toList();
    }
}
