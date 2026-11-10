package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities.CorrectiveActionPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the CorrectiveAction table; the domain only sees it through {@link CorrectiveActionRepository}.
 */
@Repository
public interface CorrectiveActionJpaRepository extends JpaRepository<CorrectiveActionPersistenceEntity, Long>, JpaSpecificationExecutor<CorrectiveActionPersistenceEntity> {

    long countByIncidentId(Long incidentId);
}
