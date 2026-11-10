package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities.IncidentRecordPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the IncidentRecord table; the domain only sees it through {@link IncidentRecordRepository}.
 */
@Repository
public interface IncidentRecordJpaRepository extends JpaRepository<IncidentRecordPersistenceEntity, Long>, JpaSpecificationExecutor<IncidentRecordPersistenceEntity> {

    List<IncidentRecordPersistenceEntity> findByLogIdAndRuleId(Long logId, Long ruleId);
}
