package com.codecraft.bevtrace.platform.incident.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.IncidentRecord;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.IncidentRecordResource;

/**
 * Builds the resource of a incident record.
 */
public final class IncidentRecordResourceFromEntityAssembler {

    private IncidentRecordResourceFromEntityAssembler() {
    }

    public static IncidentRecordResource toResourceFromEntity(IncidentRecord e) {
        return new IncidentRecordResource(e.getId(), e.getLogId(), e.getRuleId(), e.getAnomalyType(), e.getSeverity(), e.getStatus(), e.getDetectedAt(), e.getDescription(), e.getAcknowledgedBy(), e.getAcknowledgedAt(), e.getResolvedAt(), e.getReopenCount());
    }
}
