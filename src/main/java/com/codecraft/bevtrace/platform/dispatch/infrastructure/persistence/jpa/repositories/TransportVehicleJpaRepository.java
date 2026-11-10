package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.TransportVehiclePersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the TransportVehicle table; the domain only sees it through {@link TransportVehicleRepository}.
 */
@Repository
public interface TransportVehicleJpaRepository extends JpaRepository<TransportVehiclePersistenceEntity, Long>, JpaSpecificationExecutor<TransportVehiclePersistenceEntity> {
}
