package com.codecraft.bevtrace.platform.traceability.application.queryservices;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.DeliveryRecord;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetDeliveryRecordByIdQuery;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetAllDeliveryRecordsQuery;
import com.codecraft.bevtrace.platform.traceability.domain.repositories.DeliveryRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of delivery records.
 */
@Service
@Transactional(readOnly = true)
public class DeliveryRecordQueryService {

    private final DeliveryRecordRepository repository;

    public DeliveryRecordQueryService(DeliveryRecordRepository repository) {
        this.repository = repository;
    }

    public List<DeliveryRecord> handle(GetAllDeliveryRecordsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<DeliveryRecord> handle(GetDeliveryRecordByIdQuery query) {
        return repository.findById(query.id());
    }
}
