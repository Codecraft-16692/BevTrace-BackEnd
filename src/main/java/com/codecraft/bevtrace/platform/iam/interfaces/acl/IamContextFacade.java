package com.codecraft.bevtrace.platform.iam.interfaces.acl;

import com.codecraft.bevtrace.platform.iam.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * The only door other contexts use to ask the IAM context about users.
 */
@Service
@Transactional(readOnly = true)
public class IamContextFacade {

    private final UserRepository users;

    public IamContextFacade(UserRepository users) {
        this.users = users;
    }

    /**
     * @param email e-mail of the authenticated account (the principal name of a request)
     * @return the id of the user, if it exists
     */
    public Optional<Long> fetchUserIdByEmail(String email) {
        return users.findByEmail(email).map(user -> user.getId());
    }

    /**
     * @param userId candidate user id
     * @return true when the user exists
     */
    public boolean existsUserById(Long userId) {
        return users.existsById(userId);
    }
}
