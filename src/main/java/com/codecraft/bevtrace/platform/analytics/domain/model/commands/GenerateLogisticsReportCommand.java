package com.codecraft.bevtrace.platform.analytics.domain.model.commands;

import java.time.LocalDate;

/**
 * Generates and stores the logistics report of a period.
 *
 * @param userId      user that asks for it
 * @param periodStart first day
 * @param periodEnd   last day
 */
public record GenerateLogisticsReportCommand(Long userId, LocalDate periodStart, LocalDate periodEnd) {
}
