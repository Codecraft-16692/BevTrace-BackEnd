package com.codecraft.bevtrace.platform.telemetry.application.internal.schedulers;

import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.telemetry.application.commandservices.TelemetryDeviceCommandService;
import com.codecraft.bevtrace.platform.telemetry.domain.model.commands.DetectDisconnectionsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Periodically marks as disconnected the devices that stopped reporting. Off by default
 * ({@code bevtrace.telemetry.monitor.enabled}) so the demo data is not modified.
 */
@Component
@ConditionalOnProperty(name = "bevtrace.telemetry.monitor.enabled", havingValue = "true")
public class ConnectivityMonitor {

    private static final Logger log = LoggerFactory.getLogger(ConnectivityMonitor.class);

    private final TelemetryDeviceCommandService commands;
    private final Duration silence;

    public ConnectivityMonitor(TelemetryDeviceCommandService commands,
                               @Value("${bevtrace.telemetry.monitor.silence-minutes:5}") long silenceMinutes) {
        this.commands = commands;
        this.silence = Duration.ofMinutes(silenceMinutes);
    }

    @Scheduled(fixedDelayString = "${bevtrace.telemetry.monitor.period-ms:60000}")
    public void run() {
        if (commands.handle(new DetectDisconnectionsCommand(silence)) instanceof Result.Success<Integer, ?> ok
                && ok.value() > 0) {
            log.info("{} device(s) marked as disconnected", ok.value());
        }
    }
}
