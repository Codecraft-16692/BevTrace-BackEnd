package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities.ContactRequestPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the ContactRequest table; the domain only sees it through {@link ContactRequestRepository}.
 */
@Repository
public interface ContactRequestJpaRepository extends JpaRepository<ContactRequestPersistenceEntity, Long>, JpaSpecificationExecutor<ContactRequestPersistenceEntity> {
}
