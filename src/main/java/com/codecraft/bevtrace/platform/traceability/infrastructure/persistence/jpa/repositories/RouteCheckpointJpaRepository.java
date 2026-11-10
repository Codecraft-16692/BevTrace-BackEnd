package com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.entities.RouteCheckpointPersistenceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the RouteCheckpoint table; the domain only sees it through {@link RouteCheckpointRepository}.
 */
@Repository
public interface RouteCheckpointJpaRepository extends JpaRepository<RouteCheckpointPersistenceEntity, Long>, JpaSpecificationExecutor<RouteCheckpointPersistenceEntity> {

    Optional<RouteCheckpointPersistenceEntity> findFirstByLogIdOrderBySequenceDesc(Long logId);
}
