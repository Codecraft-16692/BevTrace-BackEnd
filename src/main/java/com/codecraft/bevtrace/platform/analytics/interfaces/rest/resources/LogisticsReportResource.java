package com.codecraft.bevtrace.platform.analytics.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects.KpiMetric;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * JSON representation of a logistics report (field names are the ones the Angular frontend consumes).
 */
public record LogisticsReportResource(Long id, Long userId, LocalDate periodStart, LocalDate periodEnd, Instant createdAt, List<KpiMetric> kpis) {
}
