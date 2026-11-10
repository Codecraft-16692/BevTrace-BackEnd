package com.codecraft.bevtrace.platform.analytics.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.analytics.domain.model.commands.GenerateLogisticsReportCommand;
import com.codecraft.bevtrace.platform.analytics.interfaces.rest.resources.GenerateLogisticsReportResource;

/**
 * Builds the commands of the analytics context from the request bodies.
 */
public final class LogisticsReportCommandFromResourceAssembler {

    private LogisticsReportCommandFromResourceAssembler() {
    }

    public static GenerateLogisticsReportCommand toCommandFromResource(Long userId, GenerateLogisticsReportResource resource) {
        return new GenerateLogisticsReportCommand(userId, resource.periodStart(), resource.periodEnd());
    }
}
