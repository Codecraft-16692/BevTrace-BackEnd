package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request body that adds a product to the catalogue.
 */
public record RegisterProductResource(@NotBlank @Pattern(regexp = "^[A-Za-z0-9-]{3,30}$") String skuCode,
                                      @NotBlank String name, @NotBlank String category, @Min(1) int volumeMl,
                                      @NotBlank String packagingType) {
}
