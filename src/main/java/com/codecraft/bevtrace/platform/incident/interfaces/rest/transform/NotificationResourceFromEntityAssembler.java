package com.codecraft.bevtrace.platform.incident.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.Notification;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.NotificationResource;

/**
 * Builds the resource of a notification.
 */
public final class NotificationResourceFromEntityAssembler {

    private NotificationResourceFromEntityAssembler() {
    }

    public static NotificationResource toResourceFromEntity(Notification e) {
        return new NotificationResource(e.getId(), e.getRecipientRole(), e.getType(), e.getTitle(), e.getMessage(), e.getCreatedAt(), e.getRead());
    }
}
