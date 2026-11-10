package com.codecraft.bevtrace.platform.traceability.application.internal.eventhandlers;

import com.codecraft.bevtrace.platform.dispatch.domain.model.events.DispatchDeparted;
import com.codecraft.bevtrace.platform.telemetry.domain.model.events.ReadingRecorded;
import com.codecraft.bevtrace.platform.traceability.application.commandservices.TraceabilityLogCommandService;
import com.codecraft.bevtrace.platform.traceability.domain.model.commands.StartRouteCommand;
import com.codecraft.bevtrace.platform.traceability.domain.model.commands.UpdatePositionCommand;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Reactions of the traceability context to events of other contexts. They run inside the transaction
 * that published the event, so the whole flow is atomic.
 */
@Component
public class TraceabilityEventHandlers {

    private final TraceabilityLogCommandService commands;

    public TraceabilityEventHandlers(TraceabilityLogCommandService commands) {
        this.commands = commands;
    }

    @EventListener
    public void on(DispatchDeparted event) {
        commands.handle(new StartRouteCommand(event.dispatchOrderId(), event.priority(), event.vehicleId(),
                event.vehiclePlate(), event.destinationName(), event.destinationLatitude(),
                event.destinationLongitude(), event.departedAt()));
    }

    @EventListener
    public void on(ReadingRecorded event) {
        commands.handle(new UpdatePositionCommand(event.deviceId(), event.latitude(), event.longitude()));
    }
}
