package com.codecraft.bevtrace.platform.iam.domain.model.aggregates;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;
import java.util.regex.Pattern;

/**
 * A person that uses the platform.
 */
public class User extends AbstractDomainAggregateRoot<User> {

    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$");

    private Long id;
    private Long roleId;
    private String name;
    private String email;
    private String passwordHash;
    private String phone;
    private boolean active;
    private Instant createdAt;

    protected User() {
    }

    /**
     * Registers a new, active user.
     *
     * @param roleId       role of the user
     * @param name         full name
     * @param email        unique e-mail (stored lower-case)
     * @param passwordHash BCrypt hash of the password
     * @param phone        contact phone
     * @return the new user
     */
    public static User register(Long roleId, String name, String email, String passwordHash, String phone) {
        if (email == null || !EMAIL.matcher(email.trim()).matches()) {
            throw new IllegalStateException("iam.errors.email-format");
        }
        var user = new User();
        user.roleId = roleId;
        user.name = name.trim();
        user.email = email.trim().toLowerCase();
        user.passwordHash = passwordHash;
        user.phone = phone == null ? "" : phone.trim();
        user.active = true;
        user.createdAt = Instant.now();
        return user;
    }

    /**
     * Moves the user to another role.
     *
     * @param newRoleId target role
     */
    public void assignRole(Long newRoleId) {
        this.roleId = newRoleId;
    }

    /**
     * Enables or disables the account.
     *
     * @param value new state
     */
    public void changeActive(boolean value) {
        this.active = value;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isActive() {
        return active;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static User restore(Long id, Long roleId, String name, String email, String passwordHash, String phone, boolean active, Instant createdAt) {
        var a = new User();
        a.id = id;
        a.roleId = roleId;
        a.name = name;
        a.email = email;
        a.passwordHash = passwordHash;
        a.phone = phone;
        a.active = active;
        a.createdAt = createdAt;
        return a;
    }
}
