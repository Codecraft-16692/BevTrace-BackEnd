package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.repositories;

import com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities.NewsletterSubscriberPersistenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data access to the NewsletterSubscriber table; the domain only sees it through {@link NewsletterSubscriberRepository}.
 */
@Repository
public interface NewsletterSubscriberJpaRepository extends JpaRepository<NewsletterSubscriberPersistenceEntity, Long>, JpaSpecificationExecutor<NewsletterSubscriberPersistenceEntity> {

    boolean existsByEmail(String email);
}
