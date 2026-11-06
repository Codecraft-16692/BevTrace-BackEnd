package com.codecraft.bevtrace.platform.incident.application.internal.eventhandlers;

import com.codecraft.bevtrace.platform.incident.application.commandservices.IncidentRecordCommandService;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.DetectAnomaliesCommand;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodic anomaly detection, off unless {@code bevtrace.incident.detection.enabled=true}.
 */
@Component
@ConditionalOnProperty(name = "bevtrace.incident.detection.enabled", havingValue = "true")
class AnomalyScheduler {

    private final IncidentRecordCommandService commands;

    AnomalyScheduler(IncidentRecordCommandService commands) {
        this.commands = commands;
    }

    @Scheduled(fixedDelayString = "${bevtrace.incident.detection.period-ms:60000}")
    void run() {
        commands.handle(new DetectAnomaliesCommand());
    }
}
