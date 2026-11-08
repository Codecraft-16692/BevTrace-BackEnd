package com.codecraft.bevtrace.platform.dispatch.application.internal.eventhandlers;

import com.codecraft.bevtrace.platform.dispatch.application.commandservices.DispatchOrderCommandService;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.CompleteDeliveryCommand;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.traceability.domain.model.events.DeliveryConfirmed;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Closes the dispatch when traceability confirms the delivery. It runs inside the publishing
 * transaction, so a failure rolls the whole flow back.
 */
@Component
public class DeliveryConfirmedEventHandler {

    private final DispatchOrderCommandService commandService;

    public DeliveryConfirmedEventHandler(DispatchOrderCommandService commandService) {
        this.commandService = commandService;
    }

    @EventListener
    public void on(DeliveryConfirmed event) {
        var result = commandService.handle(new CompleteDeliveryCommand(event.dispatchOrderId(), event.deliveredAt()));
        if (result instanceof Result.Failure<?, ?> failure) {
            throw new IllegalStateException(String.valueOf(failure.error()));
        }
    }
}
