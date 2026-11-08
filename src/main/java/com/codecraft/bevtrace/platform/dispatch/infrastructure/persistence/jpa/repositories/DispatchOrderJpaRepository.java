package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.DispatchOrderPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the DispatchOrder table; the domain only sees it through {@link DispatchOrderRepository}.
 */
@Repository
public interface DispatchOrderJpaRepository extends JpaRepository<DispatchOrderPersistenceEntity, Long>, JpaSpecificationExecutor<DispatchOrderPersistenceEntity> {
}
