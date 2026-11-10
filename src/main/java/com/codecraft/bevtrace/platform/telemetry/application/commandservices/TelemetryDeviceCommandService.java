package com.codecraft.bevtrace.platform.telemetry.application.commandservices;

import com.codecraft.bevtrace.platform.dispatch.interfaces.acl.DispatchContextFacade;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.DisconnectionPeriod;
import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.TelemetryDevice;
import com.codecraft.bevtrace.platform.telemetry.domain.model.commands.DetectDisconnectionsCommand;
import com.codecraft.bevtrace.platform.telemetry.domain.model.commands.ProvisionDeviceCommand;
import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.ConnectionStatus;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.DeviceModelRepository;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.DisconnectionPeriodRepository;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.TelemetryDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Commands of the devices: provisioning and the silence detection that marks them as disconnected.
 */
@Service
@Transactional
public class TelemetryDeviceCommandService {

    private final TelemetryDeviceRepository devices;
    private final DeviceModelRepository models;
    private final DisconnectionPeriodRepository periods;
    private final DispatchContextFacade dispatch;

    public TelemetryDeviceCommandService(TelemetryDeviceRepository devices, DeviceModelRepository models,
                                         DisconnectionPeriodRepository periods, DispatchContextFacade dispatch) {
        this.devices = devices;
        this.models = models;
        this.periods = periods;
        this.dispatch = dispatch;
    }

    public Result<TelemetryDevice, ApplicationError> handle(ProvisionDeviceCommand command) {
        var code = command.deviceCode().trim().toUpperCase();
        if (!models.existsById(command.modelId())) {
            return Result.failure(ApplicationError.notFound("DeviceModel", command.modelId()));
        }
        if (!dispatch.vehicleExists(command.vehicleId())) {
            return Result.failure(ApplicationError.notFound("Vehicle", command.vehicleId()));
        }
        if (devices.existsByDeviceCode(code)) {
            return Result.failure(ApplicationError.conflict("TelemetryDevice", "telemetry.errors.duplicated-code"));
        }
        if (devices.existsByVehicleId(command.vehicleId())) {
            return Result.failure(ApplicationError.conflict("TelemetryDevice", "telemetry.errors.vehicle-has-device"));
        }
        return Results.attempt(() ->
                devices.save(TelemetryDevice.provision(command.vehicleId(), command.modelId(), code)));
    }

    /**
     * @return success with the number of devices marked as disconnected
     */
    public Result<Integer, ApplicationError> handle(DetectDisconnectionsCommand command) {
        return Results.attempt(() -> {
            var silent = devices.findSilent(ConnectionStatus.CONNECTED, Instant.now().minus(command.silence()));
            for (var device : silent) {
                device.markDisconnected();
                devices.save(device);
                periods.save(DisconnectionPeriod.open(device.getId(), device.getLastSignalAt()));
            }
            return silent.size();
        });
    }
}
