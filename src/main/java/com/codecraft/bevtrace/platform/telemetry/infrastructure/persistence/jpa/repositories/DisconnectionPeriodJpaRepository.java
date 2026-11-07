package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities.DisconnectionPeriodPersistenceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the DisconnectionPeriod table; the domain only sees it through {@link DisconnectionPeriodRepository}.
 */
@Repository
public interface DisconnectionPeriodJpaRepository extends JpaRepository<DisconnectionPeriodPersistenceEntity, Long>, JpaSpecificationExecutor<DisconnectionPeriodPersistenceEntity> {

    Optional<DisconnectionPeriodPersistenceEntity> findFirstByDeviceIdAndEndedAtIsNull(Long deviceId);
}
