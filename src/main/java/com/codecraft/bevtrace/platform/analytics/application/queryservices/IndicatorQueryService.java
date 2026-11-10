package com.codecraft.bevtrace.platform.analytics.application.queryservices;

import com.codecraft.bevtrace.platform.analytics.application.internal.outboundservices.acl.ExternalOperationalDataService;
import com.codecraft.bevtrace.platform.analytics.domain.model.aggregates.LogisticsReport;
import com.codecraft.bevtrace.platform.analytics.domain.model.queries.GetIndicatorsQuery;
import com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects.Indicators;
import com.codecraft.bevtrace.platform.analytics.domain.services.KpiCalculator;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Computes OTIF, fill rate, ERI, inventory rotation and shrinkage on demand.
 */
@Service
@Transactional(readOnly = true)
public class IndicatorQueryService {

    private final ExternalOperationalDataService operationalData;

    public IndicatorQueryService(ExternalOperationalDataService operationalData) {
        this.operationalData = operationalData;
    }

    public Result<Indicators, ApplicationError> handle(GetIndicatorsQuery query) {
        return Results.attempt(() -> {
            LogisticsReport.requireValidPeriod(query.periodStart(), query.periodEnd());
            return KpiCalculator.calculate(operationalData.fetchOperationalData(), query.periodStart(), query.periodEnd());
        });
    }
}
