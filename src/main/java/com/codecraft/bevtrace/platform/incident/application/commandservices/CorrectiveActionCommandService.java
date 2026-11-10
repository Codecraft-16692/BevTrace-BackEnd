package com.codecraft.bevtrace.platform.incident.application.commandservices;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.CorrectiveAction;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.RegisterCorrectiveActionCommand;
import com.codecraft.bevtrace.platform.incident.domain.repositories.CorrectiveActionRepository;
import com.codecraft.bevtrace.platform.incident.domain.repositories.IncidentRecordRepository;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Commands on corrective actions.
 */
@Service
@Transactional
public class CorrectiveActionCommandService {

    private final IncidentRecordRepository incidents;
    private final CorrectiveActionRepository actions;

    public CorrectiveActionCommandService(IncidentRecordRepository incidents, CorrectiveActionRepository actions) {
        this.incidents = incidents;
        this.actions = actions;
    }

    public Result<CorrectiveAction, ApplicationError> handle(RegisterCorrectiveActionCommand command) {
        var incident = incidents.findById(command.incidentId());
        if (incident.isEmpty()) {
            return Result.failure(ApplicationError.notFound("IncidentRecord", command.incidentId()));
        }
        return Results.attempt(() -> {
            incident.get().requireUnresolved();
            return actions.save(CorrectiveAction.apply(command.incidentId(), command.userId(), command.description()));
        });
    }
}
