package com.codecraft.bevtrace.platform.analytics.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Request body that asks for a logistics report.
 */
public record GenerateLogisticsReportResource(@NotNull LocalDate periodStart, @NotNull LocalDate periodEnd) {
}
