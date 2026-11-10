package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.AssignVehicleCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.CancelDispatchOrderCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.ChangeDispatchPriorityCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.RegisterDepartureCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.ScheduleDispatchOrderCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.ValidateCargoCommand;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.AssignVehicleResource;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.ChangeDispatchPriorityResource;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.ScheduleDispatchOrderResource;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.ValidateCargoResource;

/**
 * Builds the dispatch commands from the request bodies.
 */
public final class DispatchOrderCommandFromResourceAssembler {

    private DispatchOrderCommandFromResourceAssembler() {
    }

    public static ScheduleDispatchOrderCommand toCommandFromResource(Long managerId, ScheduleDispatchOrderResource resource) {
        var items = resource.items().stream()
                .map(i -> new ScheduleDispatchOrderCommand.Item(i.batchId(), i.quantity())).toList();
        return new ScheduleDispatchOrderCommand(managerId, resource.destinationId(), resource.scheduledDate(),
                resource.priority(), items);
    }

    public static AssignVehicleCommand toCommandFromResource(Long orderId, AssignVehicleResource resource) {
        return new AssignVehicleCommand(orderId, resource.vehicleId());
    }

    public static ValidateCargoCommand toCommandFromResource(Long orderId, ValidateCargoResource resource) {
        return new ValidateCargoCommand(orderId, resource.scannedPallets());
    }

    public static ChangeDispatchPriorityCommand toCommandFromResource(Long orderId, ChangeDispatchPriorityResource resource) {
        return new ChangeDispatchPriorityCommand(orderId, resource.priority());
    }

    public static RegisterDepartureCommand toDepartureCommand(Long orderId) {
        return new RegisterDepartureCommand(orderId);
    }

    public static CancelDispatchOrderCommand toCancelCommand(Long orderId) {
        return new CancelDispatchOrderCommand(orderId);
    }
}
