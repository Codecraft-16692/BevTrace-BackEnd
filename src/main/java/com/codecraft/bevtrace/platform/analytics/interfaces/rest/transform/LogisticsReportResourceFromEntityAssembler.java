package com.codecraft.bevtrace.platform.analytics.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.analytics.domain.model.aggregates.LogisticsReport;
import com.codecraft.bevtrace.platform.analytics.interfaces.rest.resources.LogisticsReportResource;

/**
 * Builds the resource of a logistics report.
 */
public final class LogisticsReportResourceFromEntityAssembler {

    private LogisticsReportResourceFromEntityAssembler() {
    }

    public static LogisticsReportResource toResourceFromEntity(LogisticsReport e) {
        return new LogisticsReportResource(e.getId(), e.getUserId(), e.getPeriodStart(), e.getPeriodEnd(), e.getCreatedAt(), e.getKpis());
    }
}
