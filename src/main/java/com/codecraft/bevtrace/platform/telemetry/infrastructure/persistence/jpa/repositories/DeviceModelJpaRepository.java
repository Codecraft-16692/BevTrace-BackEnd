package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities.DeviceModelPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the DeviceModel table; the domain only sees it through {@link DeviceModelRepository}.
 */
@Repository
public interface DeviceModelJpaRepository extends JpaRepository<DeviceModelPersistenceEntity, Long>, JpaSpecificationExecutor<DeviceModelPersistenceEntity> {
}
