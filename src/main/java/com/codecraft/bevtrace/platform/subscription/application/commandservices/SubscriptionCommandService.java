package com.codecraft.bevtrace.platform.subscription.application.commandservices;

import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Payment;
import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Subscription;
import com.codecraft.bevtrace.platform.subscription.domain.model.commands.CancelSubscriptionCommand;
import com.codecraft.bevtrace.platform.subscription.domain.model.commands.SubscribeCommand;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.PaymentStatus;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.PaymentRepository;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.SubscriptionRepository;
import com.codecraft.bevtrace.platform.subscription.domain.services.CardPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Commands on subscriptions: buying a plan and cancelling it.
 */
@Service
@Transactional
public class SubscriptionCommandService {

    private static final List<SubscriptionStatus> CURRENT = List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAST_DUE);

    private final SubscriptionPlanRepository plans;
    private final SubscriptionRepository subscriptions;
    private final PaymentRepository payments;

    public SubscriptionCommandService(SubscriptionPlanRepository plans, SubscriptionRepository subscriptions,
                                      PaymentRepository payments) {
        this.plans = plans;
        this.subscriptions = subscriptions;
        this.payments = payments;
    }

    /**
     * Buys a plan. A declined card (4000000000000002) stores the pending subscription and the failed payment
     * and answers a business-rule error, so the attempt stays visible in the payment history.
     */
    public Result<Subscription, ApplicationError> handle(SubscribeCommand command) {
        var plan = plans.findByCodeAndBillingPeriod(command.planCode(), command.billingCycle()).filter(p -> !p.isCustom());
        if (plan.isEmpty()) {
            return Result.failure(ApplicationError.businessRuleViolation("plan not purchasable",
                    "subscription.errors.plan-not-purchasable"));
        }
        if (subscriptions.existsByUserIdAndStatusIn(command.userId(), CURRENT)) {
            return Result.failure(ApplicationError.conflict("Subscription", "subscription.errors.already-active"));
        }
        return Results.attempt(() -> {
            if (command.cardHolder() == null || command.cardHolder().trim().length() < 3) {
                throw new IllegalStateException("subscription.errors.holder-required");
            }
            if (!CardPolicy.isValid(command.cardNumber())) {
                throw new IllegalStateException("subscription.errors.card-invalid");
            }
            var now = Instant.now();
            var subscription = subscriptions.save(Subscription.open(command.userId(), command.companyName(), plan.get(), now));
            boolean declined = CardPolicy.isDeclined(command.cardNumber());
            if (!declined) {
                subscription.activate(now);
                subscription = subscriptions.save(subscription);
            }
            payments.save(Payment.register(subscription, plan.get(),
                    declined ? PaymentStatus.FAILED : PaymentStatus.PAID, CardPolicy.last4(command.cardNumber()), now));
            return declined ? null : subscription;
        }).flatMap(subscription -> subscription == null
                ? Result.<Subscription, ApplicationError>failure(ApplicationError.businessRuleViolation(
                        "payment declined", "subscription.errors.payment-declined"))
                : Result.<Subscription, ApplicationError>success(subscription));
    }

    public Result<Subscription, ApplicationError> handle(CancelSubscriptionCommand command) {
        var subscription = subscriptions.findById(command.subscriptionId())
                .filter(s -> command.actorIsAdmin() || command.actorUserId().equals(s.getUserId()));
        if (subscription.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Subscription", command.subscriptionId()));
        }
        return Results.attempt(() -> {
            subscription.get().cancel();
            return subscriptions.save(subscription.get());
        });
    }
}
