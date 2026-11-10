package com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.iam.domain.model.entities.Role;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Stored form of {@link Role}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "roles")
public class RolePersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String name;
    private boolean isAdmin;

    public static RolePersistenceEntity fromDomain(Role d) {
        var e = new RolePersistenceEntity();
        e.setId(d.getId());
        e.name = d.getName();
        e.isAdmin = d.isAdmin();
        return e;
    }

    public Role toDomain() {
        return Role.restore(getId(), name, isAdmin);
    }
}
