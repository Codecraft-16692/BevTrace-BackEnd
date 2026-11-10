package com.codecraft.bevtrace.platform.incident.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.incident.domain.model.commands.AcknowledgeIncidentCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.ChangeAlertRuleStatusCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.CreateAlertRuleCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.DetectAnomaliesCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.MarkNotificationReadCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.RegisterCorrectiveActionCommand;
import com.codecraft.bevtrace.platform.incident.domain.model.commands.ResolveIncidentCommand;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.CreateAlertRuleResource;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.RegisterCorrectiveActionResource;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.UpdateAlertRuleStatusResource;

/**
 * Builds the commands of the incident context from the request bodies and path variables.
 */
public final class IncidentCommandFromResourceAssembler {

    private IncidentCommandFromResourceAssembler() {
    }

    public static CreateAlertRuleCommand toCommandFromResource(CreateAlertRuleResource resource) {
        return new CreateAlertRuleCommand(resource.conditionType(), resource.threshold(), resource.severity(),
                resource.description());
    }

    public static ChangeAlertRuleStatusCommand toCommandFromResource(Long ruleId, UpdateAlertRuleStatusResource resource) {
        return new ChangeAlertRuleStatusCommand(ruleId, resource.active());
    }

    public static RegisterCorrectiveActionCommand toCommandFromResource(Long incidentId, Long userId,
                                                                        RegisterCorrectiveActionResource resource) {
        return new RegisterCorrectiveActionCommand(incidentId, userId, resource.description());
    }

    public static AcknowledgeIncidentCommand toAcknowledgeCommand(Long incidentId, Long userId) {
        return new AcknowledgeIncidentCommand(incidentId, userId);
    }

    public static ResolveIncidentCommand toResolveCommand(Long incidentId) {
        return new ResolveIncidentCommand(incidentId);
    }

    public static DetectAnomaliesCommand toDetectCommand() {
        return new DetectAnomaliesCommand();
    }

    public static MarkNotificationReadCommand toMarkReadCommand(Long notificationId) {
        return new MarkNotificationReadCommand(notificationId);
    }
}
