package com.codecraft.bevtrace.platform.subscription.application.queryservices;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.NewsletterSubscriber;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetNewsletterSubscriberByIdQuery;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetAllNewsletterSubscribersQuery;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.NewsletterSubscriberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of newsletter subscribers.
 */
@Service
@Transactional(readOnly = true)
public class NewsletterSubscriberQueryService {

    private final NewsletterSubscriberRepository repository;

    public NewsletterSubscriberQueryService(NewsletterSubscriberRepository repository) {
        this.repository = repository;
    }

    public List<NewsletterSubscriber> handle(GetAllNewsletterSubscribersQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<NewsletterSubscriber> handle(GetNewsletterSubscriberByIdQuery query) {
        return repository.findById(query.id());
    }
}
