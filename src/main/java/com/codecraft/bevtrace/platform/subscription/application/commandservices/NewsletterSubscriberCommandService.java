package com.codecraft.bevtrace.platform.subscription.application.commandservices;

import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.NewsletterSubscriber;
import com.codecraft.bevtrace.platform.subscription.domain.model.commands.SubscribeToNewsletterCommand;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.NewsletterSubscriberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Commands on the newsletter list.
 */
@Service
@Transactional
public class NewsletterSubscriberCommandService {

    private final NewsletterSubscriberRepository newsletter;

    public NewsletterSubscriberCommandService(NewsletterSubscriberRepository newsletter) {
        this.newsletter = newsletter;
    }

    public Result<NewsletterSubscriber, ApplicationError> handle(SubscribeToNewsletterCommand command) {
        var email = command.email() == null ? "" : command.email().trim().toLowerCase();
        if (newsletter.existsByEmail(email)) {
            return Result.failure(ApplicationError.conflict("Newsletter", "subscription.errors.newsletter-duplicated"));
        }
        return Results.attempt(() -> newsletter.save(NewsletterSubscriber.of(email)));
    }
}
