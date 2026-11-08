package com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.services;

import com.codecraft.bevtrace.platform.iam.domain.repositories.RoleRepository;
import com.codecraft.bevtrace.platform.iam.domain.repositories.UserRepository;
import com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Loads the user behind a token (looked up by e-mail).
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository users;
    private final RoleRepository roles;

    public UserDetailsServiceImpl(UserRepository users, RoleRepository roles) {
        this.users = users;
        this.roles = roles;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        var role = roles.findById(user.getRoleId()).orElseThrow(() -> new UsernameNotFoundException(email));
        return new UserDetailsImpl(user, role.getName());
    }
}
