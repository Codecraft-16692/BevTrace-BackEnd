package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.ContactRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Stored form of {@link ContactRequest}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "contact_requests")
public class ContactRequestPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String fullName;
    private String email;
    private String company;
    private String message;

    public static ContactRequestPersistenceEntity fromDomain(ContactRequest d) {
        var e = new ContactRequestPersistenceEntity();
        e.setId(d.getId());
        e.fullName = d.getFullName();
        e.email = d.getEmail();
        e.company = d.getCompany();
        e.message = d.getMessage();
        return e;
    }

    public ContactRequest toDomain() {
        return ContactRequest.restore(getId(), fullName, email, company, message, getCreatedAt() == null ? null : getCreatedAt().toInstant());
    }
}
