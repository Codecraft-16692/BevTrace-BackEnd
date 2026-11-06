package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.Notification;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.NotificationType;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * Stored form of {@link Notification}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "notifications")
public class NotificationPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String recipientRole;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String title;
    private String message;
    @Column(name = "is_read")
    private boolean read;

    public static NotificationPersistenceEntity fromDomain(Notification d) {
        var e = new NotificationPersistenceEntity();
        e.setId(d.getId());
        e.recipientRole = d.getRecipientRole();
        e.type = d.getType();
        e.title = d.getTitle();
        e.message = d.getMessage();
        e.read = d.getRead();
        return e;
    }

    public Notification toDomain() {
        return Notification.restore(getId(), recipientRole, type, title, message, getCreatedAt() == null ? null : getCreatedAt().toInstant(), read);
    }
}
