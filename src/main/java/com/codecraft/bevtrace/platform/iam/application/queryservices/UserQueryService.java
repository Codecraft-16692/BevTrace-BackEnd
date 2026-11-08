package com.codecraft.bevtrace.platform.iam.application.queryservices;

import com.codecraft.bevtrace.platform.iam.domain.model.aggregates.User;
import com.codecraft.bevtrace.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.codecraft.bevtrace.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.codecraft.bevtrace.platform.iam.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of users.
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository repository;

    public UserQueryService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> handle(GetAllUsersQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<User> handle(GetUserByIdQuery query) {
        return repository.findById(query.id());
    }
}
