package com.codecraft.bevtrace.platform.subscription.domain.model.aggregates;

import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.PaymentStatus;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * A charge of a subscription. Only the last four digits of the card are stored.
 */
public class Payment extends AbstractDomainAggregateRoot<Payment> {

    private Long id;
    private Long subscriptionId;
    private Long userId;
    private double amount;
    private String currency;
    private PaymentStatus status;
    private String cardLast4;
    private Instant paidAt;

    protected Payment() {
    }

    /**
     * Registers the result of a charge.
     *
     * @param subscription   charged subscription
     * @param plan           plan that was charged
     * @param status         result
     * @param cardLast4      last four digits of the card
     * @param at             charge time
     * @return the payment
     */
    public static Payment register(Subscription subscription, SubscriptionPlan plan, PaymentStatus status, String cardLast4,
                                   Instant at) {
        var payment = new Payment();
        payment.subscriptionId = subscription.getId();
        payment.userId = subscription.getUserId();
        payment.amount = plan.getPriceAmount();
        payment.currency = plan.getCurrency();
        payment.status = status;
        payment.cardLast4 = cardLast4;
        payment.paidAt = at;
        return payment;
    }

    public Long getId() {
        return id;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public Long getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getCardLast4() {
        return cardLast4;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static Payment restore(Long id, Long subscriptionId, Long userId, double amount, String currency, PaymentStatus status, String cardLast4, Instant paidAt) {
        var a = new Payment();
        a.id = id;
        a.subscriptionId = subscriptionId;
        a.userId = userId;
        a.amount = amount;
        a.currency = currency;
        a.status = status;
        a.cardLast4 = cardLast4;
        a.paidAt = paidAt;
        return a;
    }
}
