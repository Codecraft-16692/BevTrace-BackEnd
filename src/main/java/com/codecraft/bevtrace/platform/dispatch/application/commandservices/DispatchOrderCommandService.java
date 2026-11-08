package com.codecraft.bevtrace.platform.dispatch.application.commandservices;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.CargoAssignment;
import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DispatchOrder;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.AssignVehicleCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.CancelDispatchOrderCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.ChangeDispatchPriorityCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.CompleteDeliveryCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.RegisterDepartureCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.ScheduleDispatchOrderCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.commands.ValidateCargoCommand;
import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchStatus;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.CargoAssignmentRepository;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.DeliveryDestinationRepository;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.DispatchOrderRepository;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.DriverRepository;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.TransportVehicleRepository;
import com.codecraft.bevtrace.platform.inventory.interfaces.acl.InventoryContextFacade;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Commands of the dispatch order lifecycle. Every handler is one transaction.
 */
@Service
@Transactional
public class DispatchOrderCommandService {

    private static final List<DispatchStatus> HOLDING_STOCK = List.of(DispatchStatus.SCHEDULED, DispatchStatus.AUTHORIZED);

    private final DispatchOrderRepository orders;
    private final CargoAssignmentRepository cargo;
    private final DeliveryDestinationRepository destinations;
    private final TransportVehicleRepository vehicles;
    private final DriverRepository drivers;
    private final InventoryContextFacade inventory;

    public DispatchOrderCommandService(DispatchOrderRepository orders, CargoAssignmentRepository cargo,
                                       DeliveryDestinationRepository destinations, TransportVehicleRepository vehicles,
                                       DriverRepository drivers, InventoryContextFacade inventory) {
        this.orders = orders;
        this.cargo = cargo;
        this.destinations = destinations;
        this.vehicles = vehicles;
        this.drivers = drivers;
        this.inventory = inventory;
    }

    public Result<DispatchOrder, ApplicationError> handle(ScheduleDispatchOrderCommand command) {
        if (!destinations.existsById(command.destinationId())) {
            return Result.failure(ApplicationError.notFound("Destination", command.destinationId()));
        }
        return Results.attempt(() -> {
            if (command.items() == null || command.items().isEmpty()) {
                throw new IllegalStateException("dispatch.errors.no-items");
            }
            Map<Long, Integer> perBatch = new LinkedHashMap<>();
            command.items().forEach(item -> perBatch.merge(item.batchId(), item.quantity(), Integer::sum));
            perBatch.forEach(this::requireFreeStock);
            int total = perBatch.values().stream().mapToInt(Integer::intValue).sum();
            var order = orders.save(DispatchOrder.schedule(command.managerId(), command.destinationId(),
                    command.scheduledDate(), command.priority(), total));
            perBatch.forEach((batchId, quantity) -> cargo.save(CargoAssignment.of(order.getId(), batchId, quantity)));
            return order;
        });
    }

    public Result<DispatchOrder, ApplicationError> handle(AssignVehicleCommand command) {
        var order = orders.findById(command.dispatchOrderId());
        if (order.isEmpty()) {
            return Result.failure(ApplicationError.notFound("DispatchOrder", command.dispatchOrderId()));
        }
        var vehicle = vehicles.findById(command.vehicleId());
        if (vehicle.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Vehicle", command.vehicleId()));
        }
        var destination = destinations.findById(order.get().getDestinationId());
        if (destination.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Destination", order.get().getDestinationId()));
        }
        return Results.attempt(() -> {
            var driver = drivers.findById(vehicle.get().getDriverId()).orElse(null);
            order.get().assignVehicle(vehicle.get(), driver, destination.get().getClientName());
            vehicles.save(vehicle.get());
            return orders.save(order.get());
        });
    }

    public Result<DispatchOrder, ApplicationError> handle(ValidateCargoCommand command) {
        var order = orders.findById(command.dispatchOrderId());
        if (order.isEmpty()) {
            return Result.failure(ApplicationError.notFound("DispatchOrder", command.dispatchOrderId()));
        }
        return Results.attempt(() -> {
            order.get().validateCargo(expectedPallets(command.dispatchOrderId()), command.scannedPallets());
            return orders.save(order.get());
        });
    }

    public Result<DispatchOrder, ApplicationError> handle(ChangeDispatchPriorityCommand command) {
        var order = orders.findById(command.dispatchOrderId());
        if (order.isEmpty()) {
            return Result.failure(ApplicationError.notFound("DispatchOrder", command.dispatchOrderId()));
        }
        return Results.attempt(() -> {
            order.get().changePriority(command.priority());
            return orders.save(order.get());
        });
    }

    public Result<DispatchOrder, ApplicationError> handle(RegisterDepartureCommand command) {
        var order = orders.findById(command.dispatchOrderId());
        if (order.isEmpty()) {
            return Result.failure(ApplicationError.notFound("DispatchOrder", command.dispatchOrderId()));
        }
        if (order.get().getVehicleId() == null) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "dispatch.errors.not-authorized", "dispatch.errors.not-authorized"));
        }
        var vehicle = vehicles.findById(order.get().getVehicleId());
        var destination = destinations.findById(order.get().getDestinationId());
        if (vehicle.isEmpty() || destination.isEmpty()) {
            return Result.failure(ApplicationError.notFound(vehicle.isEmpty() ? "Vehicle" : "Destination",
                    vehicle.isEmpty() ? order.get().getVehicleId() : order.get().getDestinationId()));
        }
        return Results.attempt(() -> {
            order.get().depart(cargo.findByDispatchOrderId(command.dispatchOrderId()), destination.get(), vehicle.get());
            return orders.save(order.get());
        });
    }

    public Result<DispatchOrder, ApplicationError> handle(CancelDispatchOrderCommand command) {
        var order = orders.findById(command.dispatchOrderId());
        if (order.isEmpty()) {
            return Result.failure(ApplicationError.notFound("DispatchOrder", command.dispatchOrderId()));
        }
        return Results.attempt(() -> {
            var vehicle = order.get().getVehicleId() == null ? null : vehicles.findById(order.get().getVehicleId()).orElse(null);
            order.get().cancel(vehicle);
            if (vehicle != null) {
                vehicles.save(vehicle);
            }
            return orders.save(order.get());
        });
    }

    public Result<DispatchOrder, ApplicationError> handle(CompleteDeliveryCommand command) {
        var order = orders.findById(command.dispatchOrderId());
        if (order.isEmpty()) {
            return Result.failure(ApplicationError.notFound("DispatchOrder", command.dispatchOrderId()));
        }
        return Results.attempt(() -> {
            var vehicle = order.get().getVehicleId() == null ? null : vehicles.findById(order.get().getVehicleId()).orElse(null);
            order.get().complete(command.deliveredAt(), vehicle);
            if (vehicle != null) {
                vehicles.save(vehicle);
            }
            return orders.save(order.get());
        });
    }

    private void requireFreeStock(Long batchId, int quantity) {
        var batch = inventory.batch(batchId).filter(InventoryContextFacade.BatchView::available)
                .orElseThrow(() -> new IllegalStateException("dispatch.errors.batch-unavailable"));
        long free = Math.max(0, batch.currentQty() - cargo.reservedUnits(batchId, HOLDING_STOCK));
        if (quantity <= 0 || quantity > free) {
            throw new IllegalStateException("dispatch.errors.stock-insufficient");
        }
    }

    private Set<String> expectedPallets(Long orderId) {
        Set<String> expected = new LinkedHashSet<>();
        for (var line : cargo.findByDispatchOrderId(orderId)) {
            var batch = inventory.batch(line.getBatchId()).orElseThrow();
            for (int pallet = 1; pallet <= line.getPalletCount(); pallet++) {
                expected.add("%s-P%d".formatted(batch.batchNumber(), pallet));
            }
        }
        return expected;
    }
}
