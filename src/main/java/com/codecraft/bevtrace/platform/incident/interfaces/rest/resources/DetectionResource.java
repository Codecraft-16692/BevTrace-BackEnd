package com.codecraft.bevtrace.platform.incident.interfaces.rest.resources;

/**
 * Outcome of an anomaly detection run.
 */
public record DetectionResource(int created, int reopened) {
}
