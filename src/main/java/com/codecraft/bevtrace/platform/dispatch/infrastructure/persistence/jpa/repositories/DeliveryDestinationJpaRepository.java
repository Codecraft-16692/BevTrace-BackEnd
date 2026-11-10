package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities.DeliveryDestinationPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the DeliveryDestination table; the domain only sees it through {@link DeliveryDestinationRepository}.
 */
@Repository
public interface DeliveryDestinationJpaRepository extends JpaRepository<DeliveryDestinationPersistenceEntity, Long>, JpaSpecificationExecutor<DeliveryDestinationPersistenceEntity> {
}
