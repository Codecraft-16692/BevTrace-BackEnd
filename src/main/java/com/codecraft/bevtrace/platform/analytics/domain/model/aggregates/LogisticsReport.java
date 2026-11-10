package com.codecraft.bevtrace.platform.analytics.domain.model.aggregates;

import com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects.Indicators;
import com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects.KpiMetric;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A saved snapshot of the indicators of a period.
 */
public class LogisticsReport extends AbstractDomainAggregateRoot<LogisticsReport> {

    private Long id;
    private Long userId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Instant createdAt;
    private List<KpiMetric> kpis = new ArrayList<>();

    protected LogisticsReport() {
    }

    /**
     * @param periodStart first day
     * @param periodEnd   last day
     * @throws IllegalStateException when a bound is missing or the end is before the start
     */
    public static void requireValidPeriod(LocalDate periodStart, LocalDate periodEnd) {
        if (periodStart == null || periodEnd == null || periodEnd.isBefore(periodStart)) {
            throw new IllegalStateException("analytics.errors.invalid-period");
        }
    }

    /**
     * Generates a report from the indicators of a period.
     *
     * @param userId      author
     * @param periodStart first day
     * @param periodEnd   last day, not before the first
     * @param indicators  computed indicators, with data
     * @return the report
     */
    public static LogisticsReport generate(Long userId, LocalDate periodStart, LocalDate periodEnd, Indicators indicators) {
        requireValidPeriod(periodStart, periodEnd);
        if (!indicators.hasData()) {
            throw new IllegalStateException("analytics.errors.no-data");
        }
        var report = new LogisticsReport();
        report.userId = userId;
        report.periodStart = periodStart;
        report.periodEnd = periodEnd;
        report.createdAt = Instant.now();
        report.kpis = new ArrayList<>(indicators.toMetrics());
        return report;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<KpiMetric> getKpis() {
        return kpis;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static LogisticsReport restore(Long id, Long userId, LocalDate periodStart, LocalDate periodEnd, Instant createdAt, List<KpiMetric> kpis) {
        var a = new LogisticsReport();
        a.id = id;
        a.userId = userId;
        a.periodStart = periodStart;
        a.periodEnd = periodEnd;
        a.createdAt = createdAt;
        a.kpis = kpis;
        return a;
    }
}
