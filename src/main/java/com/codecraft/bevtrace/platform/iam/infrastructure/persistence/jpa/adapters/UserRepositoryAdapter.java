package com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.iam.domain.model.aggregates.User;
import com.codecraft.bevtrace.platform.iam.domain.repositories.UserRepository;
import com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;
import com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists User through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public UserRepositoryAdapter(UserJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpa.findById(id).map(UserPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public User save(User aggregate) {
        var saved = jpa.save(UserPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<User> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<UserPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(UserPersistenceEntity::toDomain).toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail(email).map(UserPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpa.existsByEmail(email);
    }
}
