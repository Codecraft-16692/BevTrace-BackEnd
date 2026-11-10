package com.codecraft.bevtrace.platform.subscription.application.queryservices;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetSubscriptionPlanByIdQuery;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetAllSubscriptionPlansQuery;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of subscription plans.
 */
@Service
@Transactional(readOnly = true)
public class SubscriptionPlanQueryService {

    private final SubscriptionPlanRepository repository;

    public SubscriptionPlanQueryService(SubscriptionPlanRepository repository) {
        this.repository = repository;
    }

    public List<SubscriptionPlan> handle(GetAllSubscriptionPlansQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<SubscriptionPlan> handle(GetSubscriptionPlanByIdQuery query) {
        return repository.findById(query.id());
    }
}
