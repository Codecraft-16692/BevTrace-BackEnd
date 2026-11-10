package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.InventoryReconciliationPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the InventoryReconciliation table; the domain only sees it through {@link InventoryReconciliationRepository}.
 */
@Repository
public interface InventoryReconciliationJpaRepository extends JpaRepository<InventoryReconciliationPersistenceEntity, Long>, JpaSpecificationExecutor<InventoryReconciliationPersistenceEntity> {
}
