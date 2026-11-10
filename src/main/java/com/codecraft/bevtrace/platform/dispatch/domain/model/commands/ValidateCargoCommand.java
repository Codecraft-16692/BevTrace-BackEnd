package com.codecraft.bevtrace.platform.dispatch.domain.model.commands;

import java.util.List;

/**
 * Checks the scanned pallets against the expected cargo.
 *
 * @param dispatchOrderId order
 * @param scannedPallets  pallet codes read at the dock
 */
public record ValidateCargoCommand(Long dispatchOrderId, List<String> scannedPallets) {
}
