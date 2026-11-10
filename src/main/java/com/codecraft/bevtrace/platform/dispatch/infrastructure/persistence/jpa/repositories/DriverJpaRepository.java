package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.DriverPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the Driver table; the domain only sees it through {@link DriverRepository}.
 */
@Repository
public interface DriverJpaRepository extends JpaRepository<DriverPersistenceEntity, Long>, JpaSpecificationExecutor<DriverPersistenceEntity> {
}
