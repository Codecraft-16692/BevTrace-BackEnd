package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.ProductBatchPersistenceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the ProductBatch table; the domain only sees it through {@link ProductBatchRepository}.
 */
@Repository
public interface ProductBatchJpaRepository extends JpaRepository<ProductBatchPersistenceEntity, Long>, JpaSpecificationExecutor<ProductBatchPersistenceEntity> {

    Optional<ProductBatchPersistenceEntity> findByBatchNumber(String batchNumber);

    boolean existsByBatchNumber(String batchNumber);
}
