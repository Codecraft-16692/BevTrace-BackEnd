package com.codecraft.bevtrace.platform.incident.application.queryservices;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.Notification;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetNotificationByIdQuery;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetAllNotificationsQuery;
import com.codecraft.bevtrace.platform.incident.domain.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of notifications.
 */
@Service
@Transactional(readOnly = true)
public class NotificationQueryService {

    private final NotificationRepository repository;

    public NotificationQueryService(NotificationRepository repository) {
        this.repository = repository;
    }

    public List<Notification> handle(GetAllNotificationsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<Notification> handle(GetNotificationByIdQuery query) {
        return repository.findById(query.id());
    }
}
