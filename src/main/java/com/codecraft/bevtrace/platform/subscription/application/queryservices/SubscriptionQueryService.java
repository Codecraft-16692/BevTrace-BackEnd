package com.codecraft.bevtrace.platform.subscription.application.queryservices;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Subscription;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetSubscriptionByIdQuery;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetAllSubscriptionsQuery;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of subscriptions.
 */
@Service
@Transactional(readOnly = true)
public class SubscriptionQueryService {

    private final SubscriptionRepository repository;

    public SubscriptionQueryService(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public List<Subscription> handle(GetAllSubscriptionsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<Subscription> handle(GetSubscriptionByIdQuery query) {
        return repository.findById(query.id());
    }
}
