package com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.inventory.domain.model.commands.ResolveDiscrepancyCommand;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.ResolveDiscrepancyResource;

/**
 * Builds the discrepancy command from the request body.
 */
public final class InventoryDiscrepancyCommandFromResourceAssembler {

    private InventoryDiscrepancyCommandFromResourceAssembler() {
    }

    public static ResolveDiscrepancyCommand toCommandFromResource(Long discrepancyId, ResolveDiscrepancyResource resource) {
        return new ResolveDiscrepancyCommand(discrepancyId, resource.note());
    }
}
