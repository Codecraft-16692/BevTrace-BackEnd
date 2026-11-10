package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Payment;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.PaymentRepository;
import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities.PaymentPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.repositories.PaymentJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists Payment through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public PaymentRepositoryAdapter(PaymentJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return jpa.findById(id).map(PaymentPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public Payment save(Payment aggregate) {
        var saved = jpa.save(PaymentPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<Payment> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<PaymentPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(PaymentPersistenceEntity::toDomain).toList();
    }
}
