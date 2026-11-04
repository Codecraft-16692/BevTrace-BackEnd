package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities.WasteRecordPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the WasteRecord table; the domain only sees it through {@link WasteRecordRepository}.
 */
@Repository
public interface WasteRecordJpaRepository extends JpaRepository<WasteRecordPersistenceEntity, Long>, JpaSpecificationExecutor<WasteRecordPersistenceEntity> {
}
