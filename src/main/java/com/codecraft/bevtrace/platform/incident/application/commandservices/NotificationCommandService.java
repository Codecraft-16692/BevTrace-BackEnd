package com.codecraft.bevtrace.platform.incident.application.commandservices;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.Notification;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.CreateNotificationCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.MarkNotificationReadCommand;
import com.codecraft.bevtrace.platform.incident.domain.repositories.NotificationRepository;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Commands on notifications.
 */
@Service
@Transactional
public class NotificationCommandService {

    private final NotificationRepository notifications;

    public NotificationCommandService(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    public Result<Notification, ApplicationError> handle(CreateNotificationCommand command) {
        return Results.attempt(() -> notifications.save(Notification.create(command.recipientRole(),
                command.type(), command.title(), command.message())));
    }

    public Result<Notification, ApplicationError> handle(MarkNotificationReadCommand command) {
        var notification = notifications.findById(command.notificationId());
        if (notification.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Notification", command.notificationId()));
        }
        return Results.attempt(() -> {
            notification.get().markRead();
            return notifications.save(notification.get());
        });
    }
}
