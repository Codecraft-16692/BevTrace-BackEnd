package com.codecraft.bevtrace.platform.dispatch.interfaces.acl;

import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchStatus;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.DispatchOrderRepository;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.TransportVehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The only door other contexts use to ask the dispatch context about orders and vehicles.
 */
@Service
@Transactional(readOnly = true)
public class DispatchContextFacade {

    /**
     * Read-only view of an order.
     *
     * @param delivered         whether it reached DELIVERED
     * @param scheduledDate     planned day
     * @param deliveredAt       hand-over time
     * @param requestedQuantity units requested
     * @param deliveredQuantity units delivered
     */
    public record OrderView(boolean delivered, LocalDate scheduledDate, Instant deliveredAt, int requestedQuantity,
                            Integer deliveredQuantity) {
    }

    private final DispatchOrderRepository orders;
    private final TransportVehicleRepository vehicles;

    public DispatchContextFacade(DispatchOrderRepository orders, TransportVehicleRepository vehicles) {
        this.orders = orders;
        this.vehicles = vehicles;
    }

    public boolean vehicleExists(Long vehicleId) {
        return vehicles.existsById(vehicleId);
    }

    public List<OrderView> orders() {
        return orders.search(Map.of(), null, null, null).stream()
                .map(o -> new OrderView(o.getStatus() == DispatchStatus.DELIVERED, o.getScheduledDate(),
                        o.getDeliveredAt(), o.getRequestedQuantity(), o.getDeliveredQuantity()))
                .toList();
    }
}
