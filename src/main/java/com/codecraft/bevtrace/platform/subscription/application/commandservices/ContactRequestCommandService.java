package com.codecraft.bevtrace.platform.subscription.application.commandservices;

import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.ContactRequest;
import com.codecraft.bevtrace.platform.subscription.domain.model.commands.SubmitContactRequestCommand;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.ContactRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Commands on contact requests.
 */
@Service
@Transactional
public class ContactRequestCommandService {

    private final ContactRequestRepository contacts;

    public ContactRequestCommandService(ContactRequestRepository contacts) {
        this.contacts = contacts;
    }

    public Result<ContactRequest, ApplicationError> handle(SubmitContactRequestCommand command) {
        return Results.attempt(() -> contacts.save(
                ContactRequest.submit(command.fullName(), command.email(), command.company(), command.message())));
    }
}
