package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities.LocationStreamPersistenceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the LocationStream table; the domain only sees it through {@link LocationStreamRepository}.
 */
@Repository
public interface LocationStreamJpaRepository extends JpaRepository<LocationStreamPersistenceEntity, Long>, JpaSpecificationExecutor<LocationStreamPersistenceEntity> {

    Optional<LocationStreamPersistenceEntity> findFirstByDeviceIdOrderByTimestampDesc(Long deviceId);
}
