package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities.AlertRulePersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the AlertRule table; the domain only sees it through {@link AlertRuleRepository}.
 */
@Repository
public interface AlertRuleJpaRepository extends JpaRepository<AlertRulePersistenceEntity, Long>, JpaSpecificationExecutor<AlertRulePersistenceEntity> {

    List<AlertRulePersistenceEntity> findByActiveTrue();
}
