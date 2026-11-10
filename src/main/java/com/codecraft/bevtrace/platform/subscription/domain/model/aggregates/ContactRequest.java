package com.codecraft.bevtrace.platform.subscription.domain.model.aggregates;

import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.EmailAddress;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * A message sent by a visitor from the landing page.
 */
public class ContactRequest extends AbstractDomainAggregateRoot<ContactRequest> {

    private Long id;
    private String fullName;
    private String email;
    private String company;
    private String message;
    private Instant createdAt;

    protected ContactRequest() {
    }

    /**
     * Creates a contact request.
     *
     * @param fullName visitor name, at least 3 characters
     * @param email    visitor e-mail
     * @param company  company, optional
     * @param message  message, at least 10 characters
     * @return the request
     */
    public static ContactRequest submit(String fullName, String email, String company, String message) {
        if (!EmailAddress.isValid(email)) {
            throw new IllegalStateException("subscription.errors.email-invalid");
        }
        if (fullName == null || fullName.trim().length() < 3 || message == null || message.trim().length() < 10) {
            throw new IllegalStateException("subscription.errors.contact-incomplete");
        }
        var request = new ContactRequest();
        request.fullName = fullName.trim();
        request.email = email.trim().toLowerCase();
        request.company = company == null ? "" : company.trim();
        request.message = message.trim();
        request.createdAt = Instant.now();
        return request;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static ContactRequest restore(Long id, String fullName, String email, String company, String message, Instant createdAt) {
        var a = new ContactRequest();
        a.id = id;
        a.fullName = fullName;
        a.email = email;
        a.company = company;
        a.message = message;
        a.createdAt = createdAt;
        return a;
    }
}
