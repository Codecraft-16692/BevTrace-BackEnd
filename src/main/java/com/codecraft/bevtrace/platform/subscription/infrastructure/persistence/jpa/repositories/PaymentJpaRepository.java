package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities.PaymentPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the Payment table; the domain only sees it through {@link PaymentRepository}.
 */
@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentPersistenceEntity, Long>, JpaSpecificationExecutor<PaymentPersistenceEntity> {
}
