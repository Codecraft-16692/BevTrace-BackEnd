package com.codecraft.bevtrace.platform.iam.application.queryservices;

import com.codecraft.bevtrace.platform.iam.domain.model.aggregates.User;
import com.codecraft.bevtrace.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.codecraft.bevtrace.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.codecraft.bevtrace.platform.iam.domain.model.entities.Role;
import com.codecraft.bevtrace.platform.iam.domain.repositories.RoleRepository;
import com.codecraft.bevtrace.platform.iam.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Reads of users.
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository repository;
    private final RoleRepository roles;

    public UserQueryService(UserRepository repository, RoleRepository roles) {
        this.repository = repository;
        this.roles = roles;
    }

    public List<User> handle(GetAllUsersQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<User> handle(GetUserByIdQuery query) {
        return repository.findById(query.id());
    }

    /**
     * Lists the users a logistics manager is allowed to manage: the warehouse operators.
     */
    public List<User> handleForManager(GetAllUsersQuery query) {
        var filters = new HashMap<>(query.filters());
        var operatorRoleId = operatorRoleId();
        filters.put("roleId", List.of(String.valueOf(operatorRoleId)));
        return repository.search(filters, query.sort(), query.order(), query.limit());
    }

    /**
     * Reads one user, only when a manager is allowed to see it (a warehouse operator).
     */
    public Optional<User> handleForManager(GetUserByIdQuery query) {
        var operatorRoleId = operatorRoleId();
        return repository.findById(query.id()).filter(user -> user.getRoleId().equals(operatorRoleId));
    }

    private Long operatorRoleId() {
        return roles.findByName(Role.WAREHOUSE_OPERATOR).map(Role::getId).orElse(-1L);
    }
}
