package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.DisconnectionPeriod;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources.DisconnectionPeriodResource;

/**
 * Builds the resource of a disconnection period.
 */
public final class DisconnectionPeriodResourceFromEntityAssembler {

    private DisconnectionPeriodResourceFromEntityAssembler() {
    }

    public static DisconnectionPeriodResource toResourceFromEntity(DisconnectionPeriod e) {
        return new DisconnectionPeriodResource(e.getId(), e.getDeviceId(), e.getStartedAt(), e.getEndedAt(), e.getDataStatus());
    }
}
