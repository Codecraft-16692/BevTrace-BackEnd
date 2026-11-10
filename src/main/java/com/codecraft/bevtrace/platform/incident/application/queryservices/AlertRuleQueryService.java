package com.codecraft.bevtrace.platform.incident.application.queryservices;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.AlertRule;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetAlertRuleByIdQuery;
import com.codecraft.bevtrace.platform.incident.domain.model.queries.GetAllAlertRulesQuery;
import com.codecraft.bevtrace.platform.incident.domain.repositories.AlertRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of alert rules.
 */
@Service
@Transactional(readOnly = true)
public class AlertRuleQueryService {

    private final AlertRuleRepository repository;

    public AlertRuleQueryService(AlertRuleRepository repository) {
        this.repository = repository;
    }

    public List<AlertRule> handle(GetAllAlertRulesQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<AlertRule> handle(GetAlertRuleByIdQuery query) {
        return repository.findById(query.id());
    }
}
