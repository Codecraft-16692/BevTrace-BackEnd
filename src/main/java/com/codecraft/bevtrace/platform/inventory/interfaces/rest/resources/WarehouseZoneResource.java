package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

/**
 * JSON representation of a warehouse zone (field names are the ones the Angular frontend consumes).
 */
public record WarehouseZoneResource(Long id, String name, String aisle, String rack) {
}
