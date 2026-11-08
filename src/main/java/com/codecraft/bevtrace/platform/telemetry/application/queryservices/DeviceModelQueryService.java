package com.codecraft.bevtrace.platform.telemetry.application.queryservices;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.DeviceModel;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetDeviceModelByIdQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetAllDeviceModelsQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.DeviceModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of device models.
 */
@Service
@Transactional(readOnly = true)
public class DeviceModelQueryService {

    private final DeviceModelRepository repository;

    public DeviceModelQueryService(DeviceModelRepository repository) {
        this.repository = repository;
    }

    public List<DeviceModel> handle(GetAllDeviceModelsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<DeviceModel> handle(GetDeviceModelByIdQuery query) {
        return repository.findById(query.id());
    }
}
