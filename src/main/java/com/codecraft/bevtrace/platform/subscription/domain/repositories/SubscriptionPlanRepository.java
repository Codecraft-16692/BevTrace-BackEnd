package com.codecraft.bevtrace.platform.subscription.domain.repositories;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored subscription plans.
 */
public interface SubscriptionPlanRepository {

    Optional<SubscriptionPlan> findById(Long id);

    boolean existsById(Long id);

    SubscriptionPlan save(SubscriptionPlan subscriptionPlan);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<SubscriptionPlan> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    Optional<SubscriptionPlan> findByCodeAndBillingPeriod(String code, BillingPeriod billingPeriod);
}
