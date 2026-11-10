package com.codecraft.bevtrace.platform.incident.application.commandservices;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.IncidentRecord;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.AcknowledgeIncidentCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.CreateNotificationCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.DetectAnomaliesCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.ResolveIncidentCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.IncidentStatus;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.NotificationType;
import com.codecraft.bevtrace.platform.incident.domain.repositories.AlertRuleRepository;
import com.codecraft.bevtrace.platform.incident.domain.repositories.CorrectiveActionRepository;
import com.codecraft.bevtrace.platform.incident.domain.repositories.IncidentRecordRepository;
import com.codecraft.bevtrace.platform.incident.domain.services.AnomalyDetector;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import com.codecraft.bevtrace.platform.telemetry.interfaces.acl.TelemetryContextFacade;
import com.codecraft.bevtrace.platform.traceability.interfaces.acl.TraceabilityContextFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;

/**
 * Commands on incidents: detection on the routes in transit, acknowledgement and resolution.
 */
@Service
@Transactional
public class IncidentRecordCommandService {

    /**
     * Outcome of a detection run.
     *
     * @param created  new incidents
     * @param reopened incidents reopened inside their window
     */
    public record Detection(int created, int reopened) {
    }

    private final AlertRuleRepository rules;
    private final IncidentRecordRepository incidents;
    private final CorrectiveActionRepository actions;
    private final NotificationCommandService notifications;
    private final TraceabilityContextFacade traceability;
    private final TelemetryContextFacade telemetry;

    public IncidentRecordCommandService(AlertRuleRepository rules, IncidentRecordRepository incidents,
                                        CorrectiveActionRepository actions, NotificationCommandService notifications,
                                        TraceabilityContextFacade traceability, TelemetryContextFacade telemetry) {
        this.rules = rules;
        this.incidents = incidents;
        this.actions = actions;
        this.notifications = notifications;
        this.traceability = traceability;
        this.telemetry = telemetry;
    }

    public Result<Detection, ApplicationError> handle(DetectAnomaliesCommand command) {
        return Results.attempt(this::detect);
    }

    public Result<IncidentRecord, ApplicationError> handle(AcknowledgeIncidentCommand command) {
        var incident = incidents.findById(command.incidentId());
        if (incident.isEmpty()) {
            return Result.failure(ApplicationError.notFound("IncidentRecord", command.incidentId()));
        }
        return Results.attempt(() -> {
            incident.get().acknowledge(command.userId());
            return incidents.save(incident.get());
        });
    }

    public Result<IncidentRecord, ApplicationError> handle(ResolveIncidentCommand command) {
        var incident = incidents.findById(command.incidentId());
        if (incident.isEmpty()) {
            return Result.failure(ApplicationError.notFound("IncidentRecord", command.incidentId()));
        }
        return Results.attempt(() -> {
            incident.get().resolve(actions.countByIncidentId(command.incidentId()));
            var saved = incidents.save(incident.get());
            var destination = traceability.destinationName(saved.getLogId()).orElse("the destination");
            notifications.handle(new CreateNotificationCommand("ROLE_WAREHOUSE_OPERATOR", NotificationType.INCIDENT,
                    "Incident resolved", "The incident on dispatch to %s was resolved.".formatted(destination)));
            return saved;
        });
    }

    private Detection detect() {
        var now = Instant.now();
        var activeRules = rules.findByActiveTrue();
        int created = 0;
        int reopened = 0;
        for (var route : traceability.inTransit()) {
            var device = route.deviceId() == null ? null : telemetry.device(route.deviceId()).orElse(null);
            var reading = route.deviceId() == null ? null : telemetry.lastReading(route.deviceId()).orElse(null);
            var signal = new AnomalyDetector.RouteSignal(route.destinationName(), route.estimatedArrival(),
                    device == null ? null : device.deviceCode(), device != null && device.disconnected(),
                    device == null ? 0 : device.minutesWithoutSignal(),
                    reading == null ? null : reading.temperature());
            for (var rule : activeRules) {
                var description = AnomalyDetector.evaluate(rule, signal, now);
                if (description.isEmpty()) {
                    continue;
                }
                var related = incidents.findByLogIdAndRuleId(route.logId(), rule.getId());
                if (related.stream().anyMatch(i -> i.getStatus() != IncidentStatus.RESOLVED)) {
                    continue;
                }
                var recent = related.stream().filter(i -> i.resolvedRecently(now))
                        .max(Comparator.comparing(IncidentRecord::getResolvedAt));
                if (recent.isPresent()) {
                    recent.get().reopen();
                    incidents.save(recent.get());
                    reopened++;
                } else {
                    incidents.save(IncidentRecord.detect(route.logId(), rule, description.get()));
                    created++;
                }
            }
        }
        if (created + reopened > 0) {
            notifications.handle(new CreateNotificationCommand("ROLE_LOGISTICS_MANAGER", NotificationType.INCIDENT,
                    "Anomalies detected",
                    "%d new and %d reopened incidents were detected on active routes.".formatted(created, reopened)));
        }
        return new Detection(created, reopened);
    }
}
