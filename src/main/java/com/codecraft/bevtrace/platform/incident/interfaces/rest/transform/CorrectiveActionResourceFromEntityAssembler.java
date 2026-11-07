package com.codecraft.bevtrace.platform.incident.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.CorrectiveAction;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.CorrectiveActionResource;

/**
 * Builds the resource of a corrective action.
 */
public final class CorrectiveActionResourceFromEntityAssembler {

    private CorrectiveActionResourceFromEntityAssembler() {
    }

    public static CorrectiveActionResource toResourceFromEntity(CorrectiveAction e) {
        return new CorrectiveActionResource(e.getId(), e.getIncidentId(), e.getUserId(), e.getDescription(), e.getAppliedAt());
    }
}
