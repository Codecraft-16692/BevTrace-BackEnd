package com.codecraft.bevtrace.platform.analytics.application.queryservices;

import com.codecraft.bevtrace.platform.analytics.domain.model.aggregates.LogisticsReport;
import com.codecraft.bevtrace.platform.analytics.domain.model.queries.GetLogisticsReportByIdQuery;
import com.codecraft.bevtrace.platform.analytics.domain.model.queries.GetAllLogisticsReportsQuery;
import com.codecraft.bevtrace.platform.analytics.domain.repositories.LogisticsReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of logistics reports.
 */
@Service
@Transactional(readOnly = true)
public class LogisticsReportQueryService {

    private final LogisticsReportRepository repository;

    public LogisticsReportQueryService(LogisticsReportRepository repository) {
        this.repository = repository;
    }

    public List<LogisticsReport> handle(GetAllLogisticsReportsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<LogisticsReport> handle(GetLogisticsReportByIdQuery query) {
        return repository.findById(query.id());
    }
}
