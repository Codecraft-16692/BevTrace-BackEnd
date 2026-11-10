package com.codecraft.bevtrace.platform.subscription.domain.repositories;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.NewsletterSubscriber;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored newsletter subscribers.
 */
public interface NewsletterSubscriberRepository {

    Optional<NewsletterSubscriber> findById(Long id);

    boolean existsById(Long id);

    NewsletterSubscriber save(NewsletterSubscriber newsletterSubscriber);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<NewsletterSubscriber> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    boolean existsByEmail(String email);
}
