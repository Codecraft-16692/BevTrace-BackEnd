package com.codecraft.bevtrace.platform.telemetry.interfaces.acl;

import com.codecraft.bevtrace.platform.telemetry.application.queryservices.LocationStreamQueryService;
import com.codecraft.bevtrace.platform.telemetry.application.queryservices.TelemetryDeviceQueryService;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetAllLocationStreamsQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetTelemetryDeviceByIdQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.model.queries.GetAllTelemetryDevicesQuery;
import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.ConnectionStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The only door other contexts use to ask the telemetry context about devices and readings.
 */
@Service
@Transactional(readOnly = true)
public class TelemetryContextFacade {

    /**
     * Connectivity of a device.
     *
     * @param id                  device
     * @param deviceCode          code printed on the device
     * @param disconnected        true when it stopped reporting
     * @param minutesWithoutSignal minutes since the last signal
     */
    public record DeviceView(Long id, String deviceCode, boolean disconnected, long minutesWithoutSignal) {
    }

    /**
     * Last reading of a device.
     *
     * @param temperature cargo temperature
     * @param timestamp   moment of the reading
     */
    public record ReadingView(double temperature, Instant timestamp) {
    }

    private final TelemetryDeviceQueryService devices;
    private final LocationStreamQueryService streams;

    public TelemetryContextFacade(TelemetryDeviceQueryService devices, LocationStreamQueryService streams) {
        this.devices = devices;
        this.streams = streams;
    }

    public Optional<Long> deviceOfVehicle(Long vehicleId) {
        var query = new GetAllTelemetryDevicesQuery(Map.of("vehicleId", List.of(String.valueOf(vehicleId))), null, null, 1);
        return devices.handle(query).stream().findFirst().map(device -> device.getId());
    }

    public Optional<DeviceView> device(Long deviceId) {
        return devices.handle(new GetTelemetryDeviceByIdQuery(deviceId))
                .map(device -> new DeviceView(device.getId(), device.getDeviceCode(),
                        device.getConnectionStatus() == ConnectionStatus.DISCONNECTED,
                        device.minutesWithoutSignal(Instant.now())));
    }

    public Optional<ReadingView> lastReading(Long deviceId) {
        var query = new GetAllLocationStreamsQuery(Map.of("deviceId", List.of(String.valueOf(deviceId))),
                "timestamp", "desc", 1);
        return streams.handle(query).stream().findFirst()
                .map(stream -> new ReadingView(stream.getTemperature(), stream.getTimestamp()));
    }
}
