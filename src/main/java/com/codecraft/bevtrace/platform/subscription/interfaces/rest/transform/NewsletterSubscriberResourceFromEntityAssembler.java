package com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.NewsletterSubscriber;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.NewsletterSubscriberResource;

/**
 * Builds the resource of a newsletter subscriber.
 */
public final class NewsletterSubscriberResourceFromEntityAssembler {

    private NewsletterSubscriberResourceFromEntityAssembler() {
    }

    public static NewsletterSubscriberResource toResourceFromEntity(NewsletterSubscriber e) {
        return new NewsletterSubscriberResource(e.getId(), e.getEmail(), e.getSubscribedAt());
    }
}
