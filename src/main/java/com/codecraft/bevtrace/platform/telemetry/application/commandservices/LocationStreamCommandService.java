package com.codecraft.bevtrace.platform.telemetry.application.commandservices;

import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.LocationStream;
import com.codecraft.bevtrace.platform.telemetry.domain.model.commands.RecordReadingCommand;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.DisconnectionPeriodRepository;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.LocationStreamRepository;
import com.codecraft.bevtrace.platform.telemetry.domain.repositories.TelemetryDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Ingestion of device readings. A reading reconnects a silent device; the saved stream publishes
 * {@code ReadingRecorded}, which moves the shipment the device follows.
 */
@Service
@Transactional
public class LocationStreamCommandService {

    private final TelemetryDeviceRepository devices;
    private final LocationStreamRepository streams;
    private final DisconnectionPeriodRepository periods;

    public LocationStreamCommandService(TelemetryDeviceRepository devices, LocationStreamRepository streams,
                                        DisconnectionPeriodRepository periods) {
        this.devices = devices;
        this.streams = streams;
        this.periods = periods;
    }

    public Result<LocationStream, ApplicationError> handle(RecordReadingCommand command) {
        var device = devices.findById(command.deviceId());
        if (device.isEmpty()) {
            return Result.failure(ApplicationError.notFound("TelemetryDevice", command.deviceId()));
        }
        return Results.attempt(() -> {
            if (!device.get().isActiveDevice()) {
                throw new IllegalStateException("telemetry.errors.device-inactive");
            }
            var at = command.timestamp() == null ? Instant.now() : command.timestamp();
            if (device.get().receiveSignal(at)) {
                periods.findFirstByDeviceIdAndEndedAtIsNull(command.deviceId()).ifPresent(period -> {
                    period.close(at);
                    periods.save(period);
                });
            }
            devices.save(device.get());
            return streams.save(LocationStream.record(command.deviceId(), command.latitude(), command.longitude(),
                    command.speed(), command.temperature(), at));
        });
    }
}
