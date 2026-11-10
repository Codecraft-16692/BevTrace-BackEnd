package com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.entities.DeliveryRecordPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the DeliveryRecord table; the domain only sees it through {@link DeliveryRecordRepository}.
 */
@Repository
public interface DeliveryRecordJpaRepository extends JpaRepository<DeliveryRecordPersistenceEntity, Long>, JpaSpecificationExecutor<DeliveryRecordPersistenceEntity> {
}
