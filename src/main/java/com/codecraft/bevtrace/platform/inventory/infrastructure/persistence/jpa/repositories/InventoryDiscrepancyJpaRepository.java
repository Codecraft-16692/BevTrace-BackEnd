package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.InventoryDiscrepancyPersistenceEntity;
import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.DiscrepancyStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the InventoryDiscrepancy table; the domain only sees it through {@link InventoryDiscrepancyRepository}.
 */
@Repository
public interface InventoryDiscrepancyJpaRepository extends JpaRepository<InventoryDiscrepancyPersistenceEntity, Long>, JpaSpecificationExecutor<InventoryDiscrepancyPersistenceEntity> {

    long countByReconciliationIdAndStatus(Long reconciliationId, DiscrepancyStatus status);
}
