package com.codecraft.bevtrace.platform.telemetry.application.queryservices;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.LocationStream;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetLocationStreamByIdQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetAllLocationStreamsQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.LocationStreamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of location streams.
 */
@Service
@Transactional(readOnly = true)
public class LocationStreamQueryService {

    private final LocationStreamRepository repository;

    public LocationStreamQueryService(LocationStreamRepository repository) {
        this.repository = repository;
    }

    public List<LocationStream> handle(GetAllLocationStreamsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<LocationStream> handle(GetLocationStreamByIdQuery query) {
        return repository.findById(query.id());
    }
}
