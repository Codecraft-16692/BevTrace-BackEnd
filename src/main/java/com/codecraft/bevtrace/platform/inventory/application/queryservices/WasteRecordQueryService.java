package com.codecraft.bevtrace.platform.inventory.application.queryservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.WasteRecord;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetWasteRecordByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllWasteRecordsQuery;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.WasteRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of waste records.
 */
@Service
@Transactional(readOnly = true)
public class WasteRecordQueryService {

    private final WasteRecordRepository repository;

    public WasteRecordQueryService(WasteRecordRepository repository) {
        this.repository = repository;
    }

    public List<WasteRecord> handle(GetAllWasteRecordsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<WasteRecord> handle(GetWasteRecordByIdQuery query) {
        return repository.findById(query.id());
    }
}
