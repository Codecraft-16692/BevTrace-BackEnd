package com.codecraft.bevtrace.platform.analytics.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.analytics.infrastructure.persistence.jpa.entities.LogisticsReportPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the LogisticsReport table; the domain only sees it through {@link LogisticsReportRepository}.
 */
@Repository
public interface LogisticsReportJpaRepository extends JpaRepository<LogisticsReportPersistenceEntity, Long>, JpaSpecificationExecutor<LogisticsReportPersistenceEntity> {
}
