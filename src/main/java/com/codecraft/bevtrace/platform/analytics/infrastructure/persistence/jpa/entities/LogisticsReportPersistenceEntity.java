package com.codecraft.bevtrace.platform.analytics.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.analytics.domain.model.aggregates.LogisticsReport;
import com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects.KpiMetric;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Stored form of {@link LogisticsReport}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "logistics_reports")
public class LogisticsReportPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long userId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "report_kpis", joinColumns = @JoinColumn(name = "report_id"))
    private List<KpiMetricEmbeddable> kpis = new ArrayList<>();

    public static LogisticsReportPersistenceEntity fromDomain(LogisticsReport d) {
        var e = new LogisticsReportPersistenceEntity();
        e.setId(d.getId());
        e.userId = d.getUserId();
        e.periodStart = d.getPeriodStart();
        e.periodEnd = d.getPeriodEnd();
        e.kpis = d.getKpis().stream().map(KpiMetricEmbeddable::fromDomain).collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        return e;
    }

    public LogisticsReport toDomain() {
        return LogisticsReport.restore(getId(), userId, periodStart, periodEnd, getCreatedAt() == null ? null : getCreatedAt().toInstant(), kpis.stream().map(KpiMetricEmbeddable::toDomain).toList());
    }
}
