package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities.NotificationPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the Notification table; the domain only sees it through {@link NotificationRepository}.
 */
@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationPersistenceEntity, Long>, JpaSpecificationExecutor<NotificationPersistenceEntity> {
}
