package com.codecraft.bevtrace.platform.inventory.domain.model.commands;

/**
 * Adds a product to the catalogue.
 *
 * @param skuCode       unique stock-keeping code
 * @param name          product name
 * @param category      category
 * @param volumeMl      volume of one unit in millilitres
 * @param packagingType packaging type
 */
public record RegisterProductCommand(String skuCode, String name, String category, int volumeMl, String packagingType) {
}
