package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources;

/**
 * JSON representation of a delivery destination (field names are the ones the Angular frontend consumes).
 */
public record DeliveryDestinationResource(Long id, String clientName, String address, double latitude, double longitude, String region) {
}
