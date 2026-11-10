package com.codecraft.bevtrace.platform.iam.domain.model.entities;

/**
 * A role of the platform ({@code ROLE_ADMIN}, {@code ROLE_LOGISTICS_MANAGER},
 * {@code ROLE_WAREHOUSE_OPERATOR}). Roles are reference data, listed publicly so the sign-up form can show them.
 */
public class Role {

    private Long id;
    private String name;
    private boolean isAdmin;

    protected Role() {
    }

    /**
     * Rebuilds a role from its stored state. Used only by the persistence adapter.
     *
     * @param id      identifier
     * @param name    authority name, e.g. ROLE_ADMIN
     * @param isAdmin whether the role has administrator rights (administrators cannot self-register)
     * @return the role
     */
    public static Role restore(Long id, String name, boolean isAdmin) {
        var role = new Role();
        role.id = id;
        role.name = name;
        role.isAdmin = isAdmin;
        return role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
