package com.codecraft.bevtrace.platform.subscription.domain.model.aggregates;

import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.EmailAddress;
import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * An e-mail subscribed to the commercial newsletter (landing page).
 */
public class NewsletterSubscriber extends AbstractDomainAggregateRoot<NewsletterSubscriber> {

    private Long id;
    private String email;
    private Instant subscribedAt;

    protected NewsletterSubscriber() {
    }

    /**
     * @param email e-mail address
     * @return the subscriber
     */
    public static NewsletterSubscriber of(String email) {
        if (!EmailAddress.isValid(email)) {
            throw new IllegalStateException("subscription.errors.email-invalid");
        }
        var subscriber = new NewsletterSubscriber();
        subscriber.email = email.trim().toLowerCase();
        subscriber.subscribedAt = Instant.now();
        return subscriber;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Instant getSubscribedAt() {
        return subscribedAt;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static NewsletterSubscriber restore(Long id, String email, Instant subscribedAt) {
        var a = new NewsletterSubscriber();
        a.id = id;
        a.email = email;
        a.subscribedAt = subscribedAt;
        return a;
    }
}
