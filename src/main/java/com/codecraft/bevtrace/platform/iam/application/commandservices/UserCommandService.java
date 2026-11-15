package com.codecraft.bevtrace.platform.iam.application.commandservices;

import com.codecraft.bevtrace.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.codecraft.bevtrace.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.codecraft.bevtrace.platform.iam.domain.model.aggregates.User;
import com.codecraft.bevtrace.platform.iam.domain.model.commands.AssignRoleCommand;
import com.codecraft.bevtrace.platform.iam.domain.model.commands.RevokeAccessCommand;
import com.codecraft.bevtrace.platform.iam.domain.model.commands.SignInCommand;
import com.codecraft.bevtrace.platform.iam.domain.model.commands.SignUpCommand;
import com.codecraft.bevtrace.platform.iam.domain.model.entities.Role;
import com.codecraft.bevtrace.platform.iam.domain.repositories.RoleRepository;
import com.codecraft.bevtrace.platform.iam.domain.repositories.UserRepository;
import com.codecraft.bevtrace.platform.iam.domain.services.PasswordPolicy;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Commands of the IAM context: sign-in, sign-up and user administration.
 */
@Service
@Transactional
public class UserCommandService {

    /**
     * A signed-in user.
     *
     * @param user  the account
     * @param role  its role
     * @param token bearer token
     */
    public record AuthenticatedUser(User user, Role role, String token) {
    }

    private final UserRepository users;
    private final RoleRepository roles;
    private final HashingService hashing;
    private final TokenService tokens;

    public UserCommandService(UserRepository users, RoleRepository roles, HashingService hashing, TokenService tokens) {
        this.users = users;
        this.roles = roles;
        this.hashing = hashing;
        this.tokens = tokens;
    }

    public Result<AuthenticatedUser, ApplicationError> handle(SignInCommand command) {
        var user = users.findByEmail(normalize(command.email()))
                .filter(candidate -> hashing.matches(command.password(), candidate.getPasswordHash()));
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.validationError("credentials", "iam.errors.invalid-credentials"));
        }
        if (!user.get().isActive()) {
            return Result.failure(ApplicationError.conflict("User", "iam.errors.account-disabled"));
        }
        var role = roles.findById(user.get().getRoleId());
        if (role.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Role", user.get().getRoleId()));
        }
        var token = tokens.generateToken(user.get(), Set.of(role.get().getName()));
        return Result.success(new AuthenticatedUser(user.get(), role.get(), token));
    }

    public Result<User, ApplicationError> handle(SignUpCommand command) {
        var role = roles.findById(command.roleId());
        if (role.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Role", command.roleId()));
        }
        var email = normalize(command.email());
        if (users.existsByEmail(email)) {
            return Result.failure(ApplicationError.conflict("User", "iam.errors.email-duplicated"));
        }
        return Results.attempt(() -> {
            PasswordPolicy.requireStrong(command.password());
            if (role.get().isAdmin()) {
                throw new IllegalStateException("iam.errors.role-not-allowed");
            }
            // Self-registered accounts wait for a permission: the manager approves operators, the admin approves managers.
            return users.save(User.register(command.roleId(), command.name(), email,
                    hashing.encode(command.password()), command.phone(), false));
        });
    }

    /**
     * Grants a role to a user and enables the account.
     */
    public Result<User, ApplicationError> handle(AssignRoleCommand command) {
        var user = users.findById(command.userId());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("User", command.userId()));
        }
        var role = roles.findById(command.roleId());
        if (role.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Role", command.roleId()));
        }
        return Results.attempt(() -> {
            requireOther(command.actorUserId(), command.userId());
            if (!command.actorAdmin()) {
                requireOperator(user.get().getRoleId());
                requireOperator(role.get().getId());
            }
            user.get().assignRole(command.roleId());
            user.get().changeActive(true);
            return users.save(user.get());
        });
    }

    /**
     * Disables an account so that it can no longer sign in.
     */
    public Result<User, ApplicationError> handle(RevokeAccessCommand command) {
        var user = users.findById(command.userId());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("User", command.userId()));
        }
        return Results.attempt(() -> {
            requireOther(command.actorUserId(), command.userId());
            if (!command.actorAdmin()) {
                requireOperator(user.get().getRoleId());
            }
            user.get().changeActive(false);
            return users.save(user.get());
        });
    }

    /** The logistics manager only manages warehouse operators. */
    private void requireOperator(Long roleId) {
        var operator = roles.findByName(Role.WAREHOUSE_OPERATOR);
        if (operator.isEmpty() || !operator.get().getId().equals(roleId)) {
            throw new IllegalStateException("iam.errors.role-not-allowed");
        }
    }

    private static void requireOther(Long actorUserId, Long userId) {
        if (actorUserId.equals(userId)) {
            throw new IllegalStateException("iam.errors.cannot-edit-self");
        }
    }

    private static String normalize(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
