package com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects;

import java.util.List;

/**
 * The logistics indicators of a period.
 *
 * @param otif              % of deliveries on time and in full
 * @param fillRate          % of the requested units that were delivered
 * @param eri               average inventory record accuracy of the reconciled counts, in %
 * @param inventoryRotation units shipped over units in stock
 * @param shrinkageRate     % of the received units lost to waste
 * @param hasData           whether the period has any activity
 */
public record Indicators(double otif, double fillRate, double eri, double inventoryRotation, double shrinkageRate,
                         boolean hasData) {

    /**
     * @return the indicators as the metric list stored in a report
     */
    public List<KpiMetric> toMetrics() {
        return List.of(
                new KpiMetric("OTIF", otif),
                new KpiMetric("FILL_RATE", fillRate),
                new KpiMetric("ERI", eri),
                new KpiMetric("INVENTORY_ROTATION", inventoryRotation),
                new KpiMetric("SHRINKAGE_RATE", shrinkageRate));
    }
}
