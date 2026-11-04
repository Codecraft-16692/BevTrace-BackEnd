package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

/**
 * JSON representation of a product (field names are the ones the Angular frontend consumes).
 */
public record ProductResource(Long id, String skuCode, String name, String category, int volumeMl, String packagingType) {
}
