package com.codecraft.bevtrace.platform.analytics.application.commandservices;

import com.codecraft.bevtrace.platform.analytics.application.internal.outboundservices.acl.ExternalOperationalDataService;
import com.codecraft.bevtrace.platform.analytics.domain.model.aggregates.LogisticsReport;
import com.codecraft.bevtrace.platform.analytics.domain.model.commands.GenerateLogisticsReportCommand;
import com.codecraft.bevtrace.platform.analytics.domain.repositories.LogisticsReportRepository;
import com.codecraft.bevtrace.platform.analytics.domain.services.KpiCalculator;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Commands on logistics reports.
 */
@Service
@Transactional
public class LogisticsReportCommandService {

    private final LogisticsReportRepository reports;
    private final ExternalOperationalDataService operationalData;

    public LogisticsReportCommandService(LogisticsReportRepository reports, ExternalOperationalDataService operationalData) {
        this.reports = reports;
        this.operationalData = operationalData;
    }

    public Result<LogisticsReport, ApplicationError> handle(GenerateLogisticsReportCommand command) {
        return Results.attempt(() -> {
            LogisticsReport.requireValidPeriod(command.periodStart(), command.periodEnd());
            var indicators = KpiCalculator.calculate(operationalData.fetchOperationalData(),
                    command.periodStart(), command.periodEnd());
            return reports.save(LogisticsReport.generate(command.userId(), command.periodStart(),
                    command.periodEnd(), indicators));
        });
    }
}
