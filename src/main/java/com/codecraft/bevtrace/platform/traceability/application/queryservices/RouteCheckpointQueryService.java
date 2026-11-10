package com.codecraft.bevtrace.platform.traceability.application.queryservices;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.RouteCheckpoint;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetRouteCheckpointByIdQuery;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetAllRouteCheckpointsQuery;
import com.codecraft.bevtrace.platform.traceability.domain.repositories.RouteCheckpointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of route checkpoints.
 */
@Service
@Transactional(readOnly = true)
public class RouteCheckpointQueryService {

    private final RouteCheckpointRepository repository;

    public RouteCheckpointQueryService(RouteCheckpointRepository repository) {
        this.repository = repository;
    }

    public List<RouteCheckpoint> handle(GetAllRouteCheckpointsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<RouteCheckpoint> handle(GetRouteCheckpointByIdQuery query) {
        return repository.findById(query.id());
    }
}
