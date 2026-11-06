package com.codecraft.bevtrace.platform.incident.application.queryservices;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.CorrectiveAction;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetCorrectiveActionByIdQuery;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetAllCorrectiveActionsQuery;
import com.codecraft.bevtrace.platform.incident.domain.repositories.CorrectiveActionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of corrective actions.
 */
@Service
@Transactional(readOnly = true)
public class CorrectiveActionQueryService {

    private final CorrectiveActionRepository repository;

    public CorrectiveActionQueryService(CorrectiveActionRepository repository) {
        this.repository = repository;
    }

    public List<CorrectiveAction> handle(GetAllCorrectiveActionsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<CorrectiveAction> handle(GetCorrectiveActionByIdQuery query) {
        return repository.findById(query.id());
    }
}
