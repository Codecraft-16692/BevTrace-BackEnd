package com.codecraft.bevtrace.platform.incident.application.commandservices;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.AlertRule;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.ChangeAlertRuleStatusCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.CreateAlertRuleCommand;
import com.codecraft.bevtrace.platform.incident.domain.repositories.AlertRuleRepository;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Commands on alert rules.
 */
@Service
@Transactional
public class AlertRuleCommandService {

    private final AlertRuleRepository rules;

    public AlertRuleCommandService(AlertRuleRepository rules) {
        this.rules = rules;
    }

    public Result<AlertRule, ApplicationError> handle(CreateAlertRuleCommand command) {
        return Results.attempt(() -> rules.save(AlertRule.create(command.conditionType(), command.threshold(),
                command.severity(), command.description())));
    }

    public Result<AlertRule, ApplicationError> handle(ChangeAlertRuleStatusCommand command) {
        var rule = rules.findById(command.ruleId());
        if (rule.isEmpty()) {
            return Result.failure(ApplicationError.notFound("AlertRule", command.ruleId()));
        }
        return Results.attempt(() -> {
            rule.get().setActive(command.active());
            return rules.save(rule.get());
        });
    }
}
