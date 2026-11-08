package com.codecraft.bevtrace.platform.dispatch.application.queryservices;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DispatchOrder;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetDispatchOrderByIdQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetAllDispatchOrdersQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.DispatchOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of dispatch orders.
 */
@Service
@Transactional(readOnly = true)
public class DispatchOrderQueryService {

    private final DispatchOrderRepository repository;

    public DispatchOrderQueryService(DispatchOrderRepository repository) {
        this.repository = repository;
    }

    public List<DispatchOrder> handle(GetAllDispatchOrdersQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<DispatchOrder> handle(GetDispatchOrderByIdQuery query) {
        return repository.findById(query.id());
    }
}
