package com.codecraft.bevtrace.platform.dispatch.domain.model.commands;

import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchPriority;

/**
 * Changes the priority of a pending dispatch.
 *
 * @param dispatchOrderId order
 * @param priority        new priority
 */
public record ChangeDispatchPriorityCommand(Long dispatchOrderId, DispatchPriority priority) {
}
