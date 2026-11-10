package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Payment;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link Payment}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "payments")
public class PaymentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long subscriptionId;
    private Long userId;
    private double amount;
    private String currency;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String cardLast4;
    private Instant paidAt;

    public static PaymentPersistenceEntity fromDomain(Payment d) {
        var e = new PaymentPersistenceEntity();
        e.setId(d.getId());
        e.subscriptionId = d.getSubscriptionId();
        e.userId = d.getUserId();
        e.amount = d.getAmount();
        e.currency = d.getCurrency();
        e.status = d.getStatus();
        e.cardLast4 = d.getCardLast4();
        e.paidAt = d.getPaidAt();
        return e;
    }

    public Payment toDomain() {
        return Payment.restore(getId(), subscriptionId, userId, amount, currency, status, cardLast4, paidAt);
    }
}
