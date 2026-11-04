package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.NewsletterSubscriber;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link NewsletterSubscriber}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "newsletter_subscribers")
public class NewsletterSubscriberPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String email;
    private Instant subscribedAt;

    public static NewsletterSubscriberPersistenceEntity fromDomain(NewsletterSubscriber d) {
        var e = new NewsletterSubscriberPersistenceEntity();
        e.setId(d.getId());
        e.email = d.getEmail();
        e.subscribedAt = d.getSubscribedAt();
        return e;
    }

    public NewsletterSubscriber toDomain() {
        return NewsletterSubscriber.restore(getId(), email, subscribedAt);
    }
}
