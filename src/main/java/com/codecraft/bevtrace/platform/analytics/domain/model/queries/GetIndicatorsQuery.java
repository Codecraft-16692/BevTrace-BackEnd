package com.codecraft.bevtrace.platform.analytics.domain.model.queries;

import java.time.LocalDate;

/**
 * Computes the indicators of a period without storing them.
 *
 * @param periodStart first day
 * @param periodEnd   last day
 */
public record GetIndicatorsQuery(LocalDate periodStart, LocalDate periodEnd) {
}
