package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DriverStatus;

/**
 * JSON representation of a driver (field names are the ones the Angular frontend consumes).
 */
public record DriverResource(Long id, String fullName, String licenseNumber, DriverStatus status) {
}
