package com.codecraft.bevtrace.platform.incident.application.queryservices;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.IncidentRecord;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetIncidentRecordByIdQuery;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetAllIncidentRecordsQuery;
import com.codecraft.bevtrace.platform.incident.domain.repositories.IncidentRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of incident records.
 */
@Service
@Transactional(readOnly = true)
public class IncidentRecordQueryService {

    private final IncidentRecordRepository repository;

    public IncidentRecordQueryService(IncidentRecordRepository repository) {
        this.repository = repository;
    }

    public List<IncidentRecord> handle(GetAllIncidentRecordsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<IncidentRecord> handle(GetIncidentRecordByIdQuery query) {
        return repository.findById(query.id());
    }
}
