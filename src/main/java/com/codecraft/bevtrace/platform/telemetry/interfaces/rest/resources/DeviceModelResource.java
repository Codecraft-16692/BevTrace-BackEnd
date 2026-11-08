package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources;

/**
 * JSON representation of a device model (field names are the ones the Angular frontend consumes).
 */
public record DeviceModelResource(Long id, String name, String manufacturer, String sensors) {
}
