package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities.SubscriptionPlanPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the SubscriptionPlan table; the domain only sees it through {@link SubscriptionPlanRepository}.
 */
@Repository
public interface SubscriptionPlanJpaRepository extends JpaRepository<SubscriptionPlanPersistenceEntity, Long>, JpaSpecificationExecutor<SubscriptionPlanPersistenceEntity> {

    Optional<SubscriptionPlanPersistenceEntity> findByCodeAndBillingPeriod(String code, BillingPeriod billingPeriod);
}
