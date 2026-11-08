package com.codecraft.bevtrace.platform.traceability.application.queryservices;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.TraceabilityLog;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetTraceabilityLogByIdQuery;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetAllTraceabilityLogsQuery;
import com.codecraft.bevtrace.platform.traceability.domain.repositories.TraceabilityLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of traceability logs.
 */
@Service
@Transactional(readOnly = true)
public class TraceabilityLogQueryService {

    private final TraceabilityLogRepository repository;

    public TraceabilityLogQueryService(TraceabilityLogRepository repository) {
        this.repository = repository;
    }

    public List<TraceabilityLog> handle(GetAllTraceabilityLogsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<TraceabilityLog> handle(GetTraceabilityLogByIdQuery query) {
        return repository.findById(query.id());
    }
}
