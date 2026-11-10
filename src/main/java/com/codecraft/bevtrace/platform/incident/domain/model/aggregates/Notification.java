package com.codecraft.bevtrace.platform.incident.domain.model.aggregates;

import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.NotificationType;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * An in-app message addressed to every user of a role. Readers only see the notifications of their own role.
 */
public class Notification extends AbstractDomainAggregateRoot<Notification> {

    private Long id;
    private String recipientRole;
    private NotificationType type;
    private String title;
    private String message;
    private Instant createdAt;
    private boolean read;

    protected Notification() {
    }

    /**
     * Creates an unread notification.
     *
     * @param recipientRole role that should read it
     * @param type          origin
     * @param title         short title
     * @param message       body
     * @return the notification
     */
    public static Notification create(String recipientRole, NotificationType type, String title, String message) {
        var notification = new Notification();
        notification.recipientRole = recipientRole;
        notification.type = type;
        notification.title = title;
        notification.message = message;
        notification.createdAt = Instant.now();
        return notification;
    }

    /**
     * Marks the notification as read.
     */
    public void markRead() {
        read = true;
    }

    public Long getId() {
        return id;
    }

    public String getRecipientRole() {
        return recipientRole;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean getRead() {
        return read;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static Notification restore(Long id, String recipientRole, NotificationType type, String title, String message, Instant createdAt, boolean read) {
        var a = new Notification();
        a.id = id;
        a.recipientRole = recipientRole;
        a.type = type;
        a.title = title;
        a.message = message;
        a.createdAt = createdAt;
        a.read = read;
        return a;
    }
}
