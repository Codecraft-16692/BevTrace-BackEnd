package com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the User table; the domain only sees it through {@link UserRepository}.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserPersistenceEntity, Long>, JpaSpecificationExecutor<UserPersistenceEntity> {

    Optional<UserPersistenceEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
