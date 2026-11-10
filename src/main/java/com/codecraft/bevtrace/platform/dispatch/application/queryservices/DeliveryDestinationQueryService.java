package com.codecraft.bevtrace.platform.dispatch.application.queryservices;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DeliveryDestination;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetDeliveryDestinationByIdQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetAllDeliveryDestinationsQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.DeliveryDestinationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of delivery destinations.
 */
@Service
@Transactional(readOnly = true)
public class DeliveryDestinationQueryService {

    private final DeliveryDestinationRepository repository;

    public DeliveryDestinationQueryService(DeliveryDestinationRepository repository) {
        this.repository = repository;
    }

    public List<DeliveryDestination> handle(GetAllDeliveryDestinationsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<DeliveryDestination> handle(GetDeliveryDestinationByIdQuery query) {
        return repository.findById(query.id());
    }
}
