package com.codecraft.bevtrace.platform.incident.domain.services;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.AlertRule;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Pure domain service that evaluates one alert rule against the situation of one active route.
 */
public final class AnomalyDetector {

    /**
     * What is known about a shipment on the road.
     *
     * @param destinationName       client name
     * @param estimatedArrival      planned arrival
     * @param deviceCode            device code, null when the truck has no device
     * @param disconnected          whether the device is disconnected
     * @param minutesWithoutSignal  minutes since the last signal (0 without device)
     * @param lastTemperature       last cargo temperature, null when unknown
     */
    public record RouteSignal(String destinationName, Instant estimatedArrival, String deviceCode,
                              boolean disconnected, long minutesWithoutSignal, Double lastTemperature) {
    }

    private AnomalyDetector() {
    }

    /**
     * @param rule   rule to check
     * @param signal situation of the route
     * @param now    reference time
     * @return the description of the anomaly, or empty when the rule is not violated
     */
    public static Optional<String> evaluate(AlertRule rule, RouteSignal signal, Instant now) {
        return switch (rule.getConditionType()) {
            case TEMPERATURE_MAX -> signal.lastTemperature() != null && signal.lastTemperature() > rule.getThreshold()
                    ? Optional.of("Cargo temperature reached %s °C (limit %s °C) on the route to %s."
                    .formatted(number(signal.lastTemperature()), number(rule.getThreshold()), signal.destinationName()))
                    : Optional.empty();
            case SIGNAL_LOSS_MINUTES -> signal.deviceCode() != null && signal.disconnected()
                    && signal.minutesWithoutSignal() > rule.getThreshold()
                    ? Optional.of("Device %s has been without signal for %d minutes (limit %s)."
                    .formatted(signal.deviceCode(), signal.minutesWithoutSignal(), number(rule.getThreshold())))
                    : Optional.empty();
            case DELAY_MINUTES -> {
                long delay = Duration.between(signal.estimatedArrival(), now).toMinutes();
                yield delay > rule.getThreshold()
                        ? Optional.of("Shipment to %s is %d minutes late (limit %s)."
                        .formatted(signal.destinationName(), delay, number(rule.getThreshold())))
                        : Optional.empty();
            }
        };
    }

    private static String number(double value) {
        return value == Math.rint(value) ? String.valueOf((long) value) : String.valueOf(value);
    }
}
