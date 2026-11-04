package com.codecraft.bevtrace.platform.subscription.domain.model.aggregates;

import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.SubscriptionStatus;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;
import java.time.ZoneOffset;

/**
 * A company's subscription to a plan. Non-administrators only see their own subscriptions.
 */
public class Subscription extends AbstractDomainAggregateRoot<Subscription> {

    private Long id;
    private Long userId;
    private String companyName;
    private String planCode;
    private BillingPeriod billingCycle;
    private SubscriptionStatus status;
    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;
    private Instant cancelledAt;

    protected Subscription() {
    }

    /**
     * Opens a subscription whose first payment is being collected.
     *
     * @param userId      owner
     * @param companyName billed company
     * @param plan        purchased plan
     * @param now         purchase time
     * @return a pending subscription
     */
    public static Subscription open(Long userId, String companyName, SubscriptionPlan plan, Instant now) {
        var subscription = new Subscription();
        subscription.userId = userId;
        subscription.companyName = companyName.trim();
        subscription.planCode = plan.getCode();
        subscription.billingCycle = plan.getBillingPeriod();
        subscription.status = SubscriptionStatus.PENDING;
        subscription.currentPeriodStart = now;
        subscription.currentPeriodEnd = now;
        return subscription;
    }

    /**
     * Activates the subscription after a successful payment, covering one billing period.
     *
     * @param now payment time
     */
    public void activate(Instant now) {
        var end = now.atZone(ZoneOffset.UTC);
        end = billingCycle == BillingPeriod.MONTHLY ? end.plusMonths(1) : end.plusYears(1);
        status = SubscriptionStatus.ACTIVE;
        currentPeriodStart = now;
        currentPeriodEnd = end.toInstant();
    }

    /**
     * Cancels an active subscription.
     */
    public void cancel() {
        if (status != SubscriptionStatus.ACTIVE && status != SubscriptionStatus.PAST_DUE) {
            throw new IllegalStateException("subscription.errors.not-active");
        }
        status = SubscriptionStatus.CANCELLED;
        cancelledAt = Instant.now();
    }

    /**
     * @return true while the subscription gives access (active or past due)
     */
    public boolean isCurrent() {
        return status == SubscriptionStatus.ACTIVE || status == SubscriptionStatus.PAST_DUE;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPlanCode() {
        return planCode;
    }

    public BillingPeriod getBillingCycle() {
        return billingCycle;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public Instant getCurrentPeriodStart() {
        return currentPeriodStart;
    }

    public Instant getCurrentPeriodEnd() {
        return currentPeriodEnd;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static Subscription restore(Long id, Long userId, String companyName, String planCode, BillingPeriod billingCycle, SubscriptionStatus status, Instant currentPeriodStart, Instant currentPeriodEnd, Instant cancelledAt) {
        var a = new Subscription();
        a.id = id;
        a.userId = userId;
        a.companyName = companyName;
        a.planCode = planCode;
        a.billingCycle = billingCycle;
        a.status = status;
        a.currentPeriodStart = currentPeriodStart;
        a.currentPeriodEnd = currentPeriodEnd;
        a.cancelledAt = cancelledAt;
        return a;
    }
}
