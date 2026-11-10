package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.ProductPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the Product table; the domain only sees it through {@link ProductRepository}.
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductPersistenceEntity, Long>, JpaSpecificationExecutor<ProductPersistenceEntity> {
}
