package com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.inventory.domain.model.commands.ReportWasteCommand;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.ReportWasteResource;

/**
 * Builds the waste command from the request body.
 */
public final class WasteRecordCommandFromResourceAssembler {

    private WasteRecordCommandFromResourceAssembler() {
    }

    public static ReportWasteCommand toCommandFromResource(Long userId, ReportWasteResource resource) {
        return new ReportWasteCommand(userId, resource.batchId(), resource.quantity(), resource.reason());
    }
}
