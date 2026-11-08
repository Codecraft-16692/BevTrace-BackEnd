package com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.model;

import com.codecraft.bevtrace.platform.iam.domain.model.aggregates.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security view of a user: the e-mail is the username and the role name the authority.
 */
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final List<GrantedAuthority> authorities;

    public UserDetailsImpl(User user, String roleName) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPasswordHash();
        this.enabled = user.isActive();
        this.authorities = List.of(new SimpleGrantedAuthority(roleName));
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
