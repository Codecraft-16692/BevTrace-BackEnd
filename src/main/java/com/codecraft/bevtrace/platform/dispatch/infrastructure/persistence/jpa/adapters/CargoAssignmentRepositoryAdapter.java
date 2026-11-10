package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.CargoAssignment;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.CargoAssignmentRepository;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.CargoAssignmentPersistenceEntity;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.repositories.CargoAssignmentJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchStatus;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists CargoAssignment through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class CargoAssignmentRepositoryAdapter implements CargoAssignmentRepository {

    private final CargoAssignmentJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public CargoAssignmentRepositoryAdapter(CargoAssignmentJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<CargoAssignment> findById(Long id) {
        return jpa.findById(id).map(CargoAssignmentPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public CargoAssignment save(CargoAssignment aggregate) {
        var saved = jpa.save(CargoAssignmentPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<CargoAssignment> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<CargoAssignmentPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(CargoAssignmentPersistenceEntity::toDomain).toList();
    }

    @Override
    public List<CargoAssignment> findByDispatchOrderId(Long dispatchOrderId) {
        return jpa.findByDispatchOrderId(dispatchOrderId).stream().map(CargoAssignmentPersistenceEntity::toDomain).toList();
    }

    @Override
    public long reservedUnits(Long batchId, Collection<DispatchStatus> statuses) {
        return jpa.reservedUnits(batchId, statuses);
    }
}
