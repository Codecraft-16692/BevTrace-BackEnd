package com.codecraft.bevtrace.platform.incident.application.internal.eventhandlers;

import com.codecraft.bevtrace.platform.dispatch.domain.model.events.DispatchAuthorized;
import com.codecraft.bevtrace.platform.incident.application.commandservices.NotificationCommandService;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.CreateNotificationCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.NotificationType;
import com.codecraft.bevtrace.platform.inventory.domain.model.events.DiscrepanciesDetected;
import com.codecraft.bevtrace.platform.traceability.domain.model.events.DeliveryConfirmed;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Reactions of the incident context to events of other contexts: each one becomes a notification.
 * They run inside the transaction that published the event.
 */
@Component
class IncidentEventHandlers {

    private final NotificationCommandService notifications;

    IncidentEventHandlers(NotificationCommandService notifications) {
        this.notifications = notifications;
    }

    @EventListener
    void on(DispatchAuthorized event) {
        notifications.handle(new CreateNotificationCommand("ROLE_WAREHOUSE_OPERATOR", NotificationType.DISPATCH,
                "Dispatch authorized", "Dispatch #%d to %s was authorized. Validate the pallets before departure."
                .formatted(event.dispatchOrderId(), event.destinationName())));
    }

    @EventListener
    void on(DeliveryConfirmed event) {
        notifications.handle(new CreateNotificationCommand("ROLE_LOGISTICS_MANAGER", NotificationType.DISPATCH,
                "Delivery completed",
                "The shipment to %s was delivered and the dispatch was closed.".formatted(event.destinationName())));
    }

    @EventListener
    void on(DiscrepanciesDetected event) {
        notifications.handle(new CreateNotificationCommand("ROLE_LOGISTICS_MANAGER", NotificationType.INVENTORY,
                "Inventory discrepancy",
                "The daily reconciliation detected %d batch discrepancies that need review.".formatted(event.count())));
    }
}
