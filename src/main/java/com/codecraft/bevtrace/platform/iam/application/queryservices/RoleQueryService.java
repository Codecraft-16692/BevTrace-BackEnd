package com.codecraft.bevtrace.platform.iam.application.queryservices;

import com.codecraft.bevtrace.platform.iam.domain.model.entities.Role;
import com.codecraft.bevtrace.platform.iam.domain.model.queries.GetRoleByIdQuery;
import com.codecraft.bevtrace.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.codecraft.bevtrace.platform.iam.domain.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of roles.
 */
@Service
@Transactional(readOnly = true)
public class RoleQueryService {

    private final RoleRepository repository;

    public RoleQueryService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> handle(GetAllRolesQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<Role> handle(GetRoleByIdQuery query) {
        return repository.findById(query.id());
    }
}
