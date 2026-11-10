package com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.iam.domain.model.entities.Role;
import com.codecraft.bevtrace.platform.iam.domain.repositories.RoleRepository;
import com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.entities.RolePersistenceEntity;
import com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.repositories.RoleJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Persists roles through Spring Data.
 */
@Repository
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository jpa;

    public RoleRepositoryAdapter(RoleJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return jpa.findById(id).map(RolePersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public Role save(Role role) {
        var saved = jpa.save(RolePersistenceEntity.fromDomain(role)).toDomain();
        return saved;
    }

    @Override
    public List<Role> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<RolePersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(RolePersistenceEntity::toDomain).toList();
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpa.findByName(name).map(RolePersistenceEntity::toDomain);
    }
}
