package com.codecraft.bevtrace.platform.inventory.domain.model.commands;

/**
 * Registers the entry of a batch at the dock.
 *
 * @param scanCode scanned label
 */
public record ReceiveBatchCommand(String scanCode) {
}
