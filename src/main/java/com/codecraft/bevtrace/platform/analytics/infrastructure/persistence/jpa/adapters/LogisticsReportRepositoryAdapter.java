package com.codecraft.bevtrace.platform.analytics.infrastructure.persistence.jpa.adapters;

import com.codecraft.bevtrace.platform.analytics.domain.model.aggregates.LogisticsReport;
import com.codecraft.bevtrace.platform.analytics.domain.repositories.LogisticsReportRepository;
import com.codecraft.bevtrace.platform.analytics.infrastructure.persistence.jpa.entities.LogisticsReportPersistenceEntity;
import com.codecraft.bevtrace.platform.analytics.infrastructure.persistence.jpa.repositories.LogisticsReportJpaRepository;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications.FilterSpecifications;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * Persists LogisticsReport through Spring Data and publishes the domain events it registered once it is stored.
 */
@Repository
public class LogisticsReportRepositoryAdapter implements LogisticsReportRepository {

    private final LogisticsReportJpaRepository jpa;
    private final ApplicationEventPublisher events;

    public LogisticsReportRepositoryAdapter(LogisticsReportJpaRepository jpa, ApplicationEventPublisher events) {
        this.jpa = jpa;
        this.events = events;
    }

    @Override
    public Optional<LogisticsReport> findById(Long id) {
        return jpa.findById(id).map(LogisticsReportPersistenceEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public LogisticsReport save(LogisticsReport aggregate) {
        var saved = jpa.save(LogisticsReportPersistenceEntity.fromDomain(aggregate)).toDomain();
        aggregate.domainEvents().forEach(events::publishEvent);
        aggregate.clearDomainEvents();
        return saved;
    }

    @Override
    public List<LogisticsReport> search(Map<String, List<String>> filters, String sort, String order, Integer limit) {
        return jpa.findAll(FilterSpecifications.<LogisticsReportPersistenceEntity>of(filters), FilterSpecifications.page(sort, order, limit))
                .stream().map(LogisticsReportPersistenceEntity::toDomain).toList();
    }
}
