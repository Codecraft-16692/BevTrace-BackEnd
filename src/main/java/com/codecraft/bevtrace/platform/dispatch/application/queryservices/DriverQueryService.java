package com.codecraft.bevtrace.platform.dispatch.application.queryservices;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.Driver;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetDriverByIdQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetAllDriversQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of drivers.
 */
@Service
@Transactional(readOnly = true)
public class DriverQueryService {

    private final DriverRepository repository;

    public DriverQueryService(DriverRepository repository) {
        this.repository = repository;
    }

    public List<Driver> handle(GetAllDriversQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<Driver> handle(GetDriverByIdQuery query) {
        return repository.findById(query.id());
    }
}
