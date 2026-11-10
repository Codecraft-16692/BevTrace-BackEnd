package com.codecraft.bevtrace.platform.telemetry.application.queryservices;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.DisconnectionPeriod;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetDisconnectionPeriodByIdQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetAllDisconnectionPeriodsQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.DisconnectionPeriodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of disconnection periods.
 */
@Service
@Transactional(readOnly = true)
public class DisconnectionPeriodQueryService {

    private final DisconnectionPeriodRepository repository;

    public DisconnectionPeriodQueryService(DisconnectionPeriodRepository repository) {
        this.repository = repository;
    }

    public List<DisconnectionPeriod> handle(GetAllDisconnectionPeriodsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<DisconnectionPeriod> handle(GetDisconnectionPeriodByIdQuery query) {
        return repository.findById(query.id());
    }
}
