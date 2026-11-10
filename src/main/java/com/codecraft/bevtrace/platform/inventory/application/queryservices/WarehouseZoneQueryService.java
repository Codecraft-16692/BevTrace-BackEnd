package com.codecraft.bevtrace.platform.inventory.application.queryservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.WarehouseZone;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetWarehouseZoneByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllWarehouseZonesQuery;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.WarehouseZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of warehouse zones.
 */
@Service
@Transactional(readOnly = true)
public class WarehouseZoneQueryService {

    private final WarehouseZoneRepository repository;

    public WarehouseZoneQueryService(WarehouseZoneRepository repository) {
        this.repository = repository;
    }

    public List<WarehouseZone> handle(GetAllWarehouseZonesQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<WarehouseZone> handle(GetWarehouseZoneByIdQuery query) {
        return repository.findById(query.id());
    }
}
