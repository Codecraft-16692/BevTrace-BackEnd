package com.codecraft.bevtrace.platform.traceability.application.commandservices;

import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import com.codecraft.bevtrace.platform.telemetry.interfaces.acl.TelemetryContextFacade;
import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.DeliveryRecord;
import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.RouteCheckpoint;
import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.TraceabilityLog;
import com.codecraft.bevtrace.platform.traceability.domain.model.commands.ConfirmDeliveryCommand;
import com.codecraft.bevtrace.platform.traceability.domain.model.commands.RejectDeliveryCommand;
import com.codecraft.bevtrace.platform.traceability.domain.model.commands.StartRouteCommand;
import com.codecraft.bevtrace.platform.traceability.domain.model.commands.UpdatePositionCommand;
import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.LogStatus;
import com.codecraft.bevtrace.platform.traceability.domain.repositories.DeliveryRecordRepository;
import com.codecraft.bevtrace.platform.traceability.domain.repositories.RouteCheckpointRepository;
import com.codecraft.bevtrace.platform.traceability.domain.repositories.TraceabilityLogRepository;
import com.codecraft.bevtrace.platform.traceability.domain.services.RoutePlanner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Commands of the shipment log: start the route, follow the vehicle and close the delivery.
 * The log owns its checkpoints and delivery record, so they are handled here.
 */
@Service
@Transactional
public class TraceabilityLogCommandService {

    private final TraceabilityLogRepository logs;
    private final RouteCheckpointRepository checkpoints;
    private final DeliveryRecordRepository records;
    private final TelemetryContextFacade telemetry;

    public TraceabilityLogCommandService(TraceabilityLogRepository logs, RouteCheckpointRepository checkpoints,
                                         DeliveryRecordRepository records, TelemetryContextFacade telemetry) {
        this.logs = logs;
        this.checkpoints = checkpoints;
        this.records = records;
        this.telemetry = telemetry;
    }

    /**
     * Opens the log and plans the checkpoints. Idempotent: a dispatch that already has a log is ignored.
     */
    public Result<TraceabilityLog, ApplicationError> handle(StartRouteCommand command) {
        var existing = logs.findByDispatchOrderId(command.dispatchOrderId());
        if (existing.isPresent()) {
            return Result.success(existing.get());
        }
        return Results.attempt(() -> {
            var travel = RoutePlanner.estimateTravel(command.destinationLatitude(), command.destinationLongitude());
            var log = logs.save(TraceabilityLog.start(command.dispatchOrderId(), command.priority(),
                    command.destinationName(), command.vehiclePlate(),
                    telemetry.deviceOfVehicle(command.vehicleId()).orElse(null),
                    command.departedAt(), command.departedAt().plus(travel)));
            for (var point : RoutePlanner.plan(command.destinationName(), command.destinationLatitude(),
                    command.destinationLongitude())) {
                checkpoints.save(RouteCheckpoint.plan(log.getId(), point,
                        point.sequence() == 1 ? command.departedAt() : null));
            }
            return log;
        });
    }

    public Result<TraceabilityLog, ApplicationError> handle(ConfirmDeliveryCommand command) {
        var log = logs.findById(command.logId());
        if (log.isEmpty()) {
            return Result.failure(ApplicationError.notFound("TraceabilityLog", command.logId()));
        }
        return Results.attempt(() -> {
            var now = Instant.now();
            var record = DeliveryRecord.delivered(command.logId(), command.receivedBy(), now);
            log.get().complete(now);
            reachDestination(command.logId(), now);
            records.save(record);
            return logs.save(log.get());
        });
    }

    public Result<TraceabilityLog, ApplicationError> handle(RejectDeliveryCommand command) {
        var log = logs.findById(command.logId());
        if (log.isEmpty()) {
            return Result.failure(ApplicationError.notFound("TraceabilityLog", command.logId()));
        }
        return Results.attempt(() -> {
            var now = Instant.now();
            log.get().reject(command.reason());
            reachDestination(command.logId(), now);
            records.save(DeliveryRecord.rejected(command.logId(), command.reason(), now));
            return logs.save(log.get());
        });
    }

    /**
     * Moves the in-transit shipment followed by the device, if any.
     */
    public Result<Boolean, ApplicationError> handle(UpdatePositionCommand command) {
        return Results.attemptVoid(() ->
                logs.findFirstByDeviceIdAndCurrentStatus(command.deviceId(), LogStatus.IN_TRANSIT).ifPresent(log -> {
                    log.moveTo(command.latitude(), command.longitude());
                    logs.save(log);
                }));
    }

    private void reachDestination(Long logId, Instant at) {
        checkpoints.findFirstByLogIdOrderBySequenceDesc(logId).ifPresent(destination -> {
            destination.reach(at);
            checkpoints.save(destination);
        });
    }
}
