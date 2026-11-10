package com.codecraft.bevtrace.platform.analytics.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects.KpiMetric;
import jakarta.persistence.Embeddable;

/**
 * Stored form of {@link KpiMetric}.
 */
@Embeddable
public class KpiMetricEmbeddable {

    private String metricName;
    private double actualValue;

    static KpiMetricEmbeddable fromDomain(KpiMetric d) {
        var e = new KpiMetricEmbeddable();
        e.metricName = d.metricName();
        e.actualValue = d.actualValue();
        return e;
    }

    KpiMetric toDomain() {
        return new KpiMetric(metricName, actualValue);
    }
}
