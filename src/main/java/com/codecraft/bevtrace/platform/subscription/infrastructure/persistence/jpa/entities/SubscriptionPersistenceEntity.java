package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Subscription;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link Subscription}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "subscriptions")
public class SubscriptionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long userId;
    private String companyName;
    private String planCode;
    @Enumerated(EnumType.STRING)
    private BillingPeriod billingCycle;
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;
    private Instant cancelledAt;

    public static SubscriptionPersistenceEntity fromDomain(Subscription d) {
        var e = new SubscriptionPersistenceEntity();
        e.setId(d.getId());
        e.userId = d.getUserId();
        e.companyName = d.getCompanyName();
        e.planCode = d.getPlanCode();
        e.billingCycle = d.getBillingCycle();
        e.status = d.getStatus();
        e.currentPeriodStart = d.getCurrentPeriodStart();
        e.currentPeriodEnd = d.getCurrentPeriodEnd();
        e.cancelledAt = d.getCancelledAt();
        return e;
    }

    public Subscription toDomain() {
        return Subscription.restore(getId(), userId, companyName, planCode, billingCycle, status, currentPeriodStart, currentPeriodEnd, cancelledAt);
    }
}
