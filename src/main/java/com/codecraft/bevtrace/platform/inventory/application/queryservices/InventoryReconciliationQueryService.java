package com.codecraft.bevtrace.platform.inventory.application.queryservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryReconciliation;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetInventoryReconciliationByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllInventoryReconciliationsQuery;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.InventoryReconciliationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of inventory reconciliations.
 */
@Service
@Transactional(readOnly = true)
public class InventoryReconciliationQueryService {

    private final InventoryReconciliationRepository repository;

    public InventoryReconciliationQueryService(InventoryReconciliationRepository repository) {
        this.repository = repository;
    }

    public List<InventoryReconciliation> handle(GetAllInventoryReconciliationsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<InventoryReconciliation> handle(GetInventoryReconciliationByIdQuery query) {
        return repository.findById(query.id());
    }
}
