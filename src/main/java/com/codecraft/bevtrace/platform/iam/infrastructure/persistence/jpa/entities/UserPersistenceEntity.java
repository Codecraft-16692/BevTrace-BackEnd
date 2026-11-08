package com.codecraft.bevtrace.platform.iam.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.iam.domain.model.aggregates.User;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Stored form of {@link User}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "users")
public class UserPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long roleId;
    private String name;
    private String email;
    private String passwordHash;
    private String phone;
    private boolean active;

    public static UserPersistenceEntity fromDomain(User d) {
        var e = new UserPersistenceEntity();
        e.setId(d.getId());
        e.roleId = d.getRoleId();
        e.name = d.getName();
        e.email = d.getEmail();
        e.passwordHash = d.getPasswordHash();
        e.phone = d.getPhone();
        e.active = d.isActive();
        return e;
    }

    public User toDomain() {
        return User.restore(getId(), roleId, name, email, passwordHash, phone, active, getCreatedAt() == null ? null : getCreatedAt().toInstant());
    }
}
