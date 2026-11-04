package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the Subscription table; the domain only sees it through {@link SubscriptionRepository}.
 */
@Repository
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionPersistenceEntity, Long>, JpaSpecificationExecutor<SubscriptionPersistenceEntity> {

    boolean existsByUserIdAndStatusIn(Long userId, Collection<SubscriptionStatus> statuses);
}
