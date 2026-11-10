package com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.WarehouseZone;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.WarehouseZoneResource;

/**
 * Builds the resource of a warehouse zone.
 */
public final class WarehouseZoneResourceFromEntityAssembler {

    private WarehouseZoneResourceFromEntityAssembler() {
    }

    public static WarehouseZoneResource toResourceFromEntity(WarehouseZone e) {
        return new WarehouseZoneResource(e.getId(), e.getName(), e.getAisle(), e.getRack());
    }
}
