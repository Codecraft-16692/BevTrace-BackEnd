package com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.ContactRequest;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.ContactRequestResource;

/**
 * Builds the resource of a contact request.
 */
public final class ContactRequestResourceFromEntityAssembler {

    private ContactRequestResourceFromEntityAssembler() {
    }

    public static ContactRequestResource toResourceFromEntity(ContactRequest e) {
        return new ContactRequestResource(e.getId(), e.getFullName(), e.getEmail(), e.getCompany(), e.getMessage(), e.getCreatedAt());
    }
}
