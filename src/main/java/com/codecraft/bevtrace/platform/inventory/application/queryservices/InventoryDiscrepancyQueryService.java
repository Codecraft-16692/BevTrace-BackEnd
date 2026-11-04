package com.codecraft.bevtrace.platform.inventory.application.queryservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryDiscrepancy;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetInventoryDiscrepancyByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllInventoryDiscrepanciesQuery;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.InventoryDiscrepancyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of inventory discrepancys.
 */
@Service
@Transactional(readOnly = true)
public class InventoryDiscrepancyQueryService {

    private final InventoryDiscrepancyRepository repository;

    public InventoryDiscrepancyQueryService(InventoryDiscrepancyRepository repository) {
        this.repository = repository;
    }

    public List<InventoryDiscrepancy> handle(GetAllInventoryDiscrepanciesQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<InventoryDiscrepancy> handle(GetInventoryDiscrepancyByIdQuery query) {
        return repository.findById(query.id());
    }
}
