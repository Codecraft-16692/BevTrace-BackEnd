package com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects;

/**
 * One indicator value stored in a report.
 *
 * @param metricName  OTIF, FILL_RATE, ERI, INVENTORY_ROTATION or SHRINKAGE_RATE
 * @param actualValue value of the indicator
 */
public record KpiMetric(String metricName, double actualValue) {
}
