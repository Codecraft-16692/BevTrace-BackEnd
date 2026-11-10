package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.Driver;
import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DriverStatus;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * Stored form of {@link Driver}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "drivers")
public class DriverPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String fullName;
    private String licenseNumber;
    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    public static DriverPersistenceEntity fromDomain(Driver d) {
        var e = new DriverPersistenceEntity();
        e.setId(d.getId());
        e.fullName = d.getFullName();
        e.licenseNumber = d.getLicenseNumber();
        e.status = d.getStatus();
        return e;
    }

    public Driver toDomain() {
        return Driver.restore(getId(), fullName, licenseNumber, status);
    }
}
