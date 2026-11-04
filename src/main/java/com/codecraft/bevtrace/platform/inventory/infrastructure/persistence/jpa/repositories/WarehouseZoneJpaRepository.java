package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.WarehouseZonePersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the WarehouseZone table; the domain only sees it through {@link WarehouseZoneRepository}.
 */
@Repository
public interface WarehouseZoneJpaRepository extends JpaRepository<WarehouseZonePersistenceEntity, Long>, JpaSpecificationExecutor<WarehouseZonePersistenceEntity> {
}
