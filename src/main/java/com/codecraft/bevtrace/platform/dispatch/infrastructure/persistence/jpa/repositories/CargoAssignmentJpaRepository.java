package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.CargoAssignmentPersistenceEntity;
import java.util.List;
import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchStatus;
import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.DispatchOrderPersistenceEntity;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the CargoAssignment table; the domain only sees it through {@link CargoAssignmentRepository}.
 */
@Repository
public interface CargoAssignmentJpaRepository extends JpaRepository<CargoAssignmentPersistenceEntity, Long>, JpaSpecificationExecutor<CargoAssignmentPersistenceEntity> {

    List<CargoAssignmentPersistenceEntity> findByDispatchOrderId(Long dispatchOrderId);

    @Query("""
            select coalesce(sum(c.quantity), 0) from CargoAssignmentPersistenceEntity c, DispatchOrderPersistenceEntity o
            where c.dispatchOrderId = o.id and c.batchId = :batchId and o.status in :statuses""")
    long reservedUnits(@Param("batchId") Long batchId, @Param("statuses") Collection<DispatchStatus> statuses);
}
