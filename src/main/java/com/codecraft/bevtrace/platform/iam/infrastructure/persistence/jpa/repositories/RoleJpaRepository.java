package com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.entities.RolePersistenceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the Role table; the domain only sees it through {@link RoleRepository}.
 */
@Repository
public interface RoleJpaRepository extends JpaRepository<RolePersistenceEntity, Long>, JpaSpecificationExecutor<RolePersistenceEntity> {

    Optional<RolePersistenceEntity> findByName(String name);
}
