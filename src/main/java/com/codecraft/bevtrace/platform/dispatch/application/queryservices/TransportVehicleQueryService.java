package com.codecraft.bevtrace.platform.dispatch.application.queryservices;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.TransportVehicle;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetTransportVehicleByIdQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetAllTransportVehiclesQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.TransportVehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of transport vehicles.
 */
@Service
@Transactional(readOnly = true)
public class TransportVehicleQueryService {

    private final TransportVehicleRepository repository;

    public TransportVehicleQueryService(TransportVehicleRepository repository) {
        this.repository = repository;
    }

    public List<TransportVehicle> handle(GetAllTransportVehiclesQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<TransportVehicle> handle(GetTransportVehicleByIdQuery query) {
        return repository.findById(query.id());
    }
}
