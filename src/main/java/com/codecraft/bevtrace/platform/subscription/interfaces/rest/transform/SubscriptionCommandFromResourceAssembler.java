package com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.subscription.domain.model.commands.CancelSubscriptionCommand;
import com.codecraft.bevtrace.platform.subscription.domain.model.commands.SubmitContactRequestCommand;
import com.codecraft.bevtrace.platform.subscription.domain.model.commands.SubscribeCommand;
import com.codecraft.bevtrace.platform.subscription.domain.model.commands.SubscribeToNewsletterCommand;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.NewsletterSubscriptionResource;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.SubmitContactRequestResource;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.SubscribeResource;

/**
 * Builds the commands of the subscription context from the request bodies.
 */
public final class SubscriptionCommandFromResourceAssembler {

    private SubscriptionCommandFromResourceAssembler() {
    }

    public static SubscribeCommand toCommandFromResource(Long userId, SubscribeResource resource) {
        return new SubscribeCommand(userId, resource.companyName(), resource.planCode(), resource.billingCycle(),
                resource.cardHolder(), resource.cardNumber());
    }

    public static CancelSubscriptionCommand toCancelCommand(Long actorUserId, boolean admin, Long subscriptionId) {
        return new CancelSubscriptionCommand(actorUserId, admin, subscriptionId);
    }

    public static SubscribeToNewsletterCommand toCommandFromResource(NewsletterSubscriptionResource resource) {
        return new SubscribeToNewsletterCommand(resource.email());
    }

    public static SubmitContactRequestCommand toCommandFromResource(SubmitContactRequestResource resource) {
        return new SubmitContactRequestCommand(resource.fullName(), resource.email(), resource.company(), resource.message());
    }
}
