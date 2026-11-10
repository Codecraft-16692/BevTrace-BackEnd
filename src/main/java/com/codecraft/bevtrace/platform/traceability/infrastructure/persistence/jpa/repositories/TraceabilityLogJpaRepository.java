package com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.entities.TraceabilityLogPersistenceEntity;
import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.LogStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the TraceabilityLog table; the domain only sees it through {@link TraceabilityLogRepository}.
 */
@Repository
public interface TraceabilityLogJpaRepository extends JpaRepository<TraceabilityLogPersistenceEntity, Long>, JpaSpecificationExecutor<TraceabilityLogPersistenceEntity> {

    Optional<TraceabilityLogPersistenceEntity> findByDispatchOrderId(Long dispatchOrderId);

    List<TraceabilityLogPersistenceEntity> findByCurrentStatus(LogStatus status);

    Optional<TraceabilityLogPersistenceEntity> findFirstByDeviceIdAndCurrentStatus(Long deviceId, LogStatus status);
}
