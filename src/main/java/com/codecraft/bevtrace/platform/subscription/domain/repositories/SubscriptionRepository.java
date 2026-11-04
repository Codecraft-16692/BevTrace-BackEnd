package com.codecraft.bevtrace.platform.subscription.domain.repositories;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Subscription;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored subscriptions.
 */
public interface SubscriptionRepository {

    Optional<Subscription> findById(Long id);

    boolean existsById(Long id);

    Subscription save(Subscription subscription);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<Subscription> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    boolean existsByUserIdAndStatusIn(Long userId, Collection<SubscriptionStatus> statuses);
}
