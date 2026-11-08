package com.codecraft.bevtrace.platform.telemetry.application.queryservices;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.TelemetryDevice;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetTelemetryDeviceByIdQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetAllTelemetryDevicesQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.TelemetryDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of telemetry devices.
 */
@Service
@Transactional(readOnly = true)
public class TelemetryDeviceQueryService {

    private final TelemetryDeviceRepository repository;

    public TelemetryDeviceQueryService(TelemetryDeviceRepository repository) {
        this.repository = repository;
    }

    public List<TelemetryDevice> handle(GetAllTelemetryDevicesQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<TelemetryDevice> handle(GetTelemetryDeviceByIdQuery query) {
        return repository.findById(query.id());
    }
}
