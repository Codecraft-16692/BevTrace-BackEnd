package com.codecraft.bevtrace.platform.inventory.application.commandservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryDiscrepancy;
import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryReconciliation;
import com.codecraft.bevtrace.platform.inventory.domain.model.commands.ConfirmReconciliationCommand;
import com.codecraft.bevtrace.platform.inventory.domain.model.commands.RunReconciliationCommand;
import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.DiscrepancyStatus;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.InventoryDiscrepancyRepository;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.InventoryReconciliationRepository;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductBatchRepository;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Commands on reconciliations: running a physical count and confirming it.
 */
@Service
@Transactional
public class InventoryReconciliationCommandService {

    private final InventoryReconciliationRepository reconciliations;
    private final InventoryDiscrepancyRepository discrepancies;
    private final ProductBatchRepository batches;

    public InventoryReconciliationCommandService(InventoryReconciliationRepository reconciliations,
                                                 InventoryDiscrepancyRepository discrepancies,
                                                 ProductBatchRepository batches) {
        this.reconciliations = reconciliations;
        this.discrepancies = discrepancies;
        this.batches = batches;
    }

    public Result<InventoryReconciliation, ApplicationError> handle(RunReconciliationCommand command) {
        if (command.counts() == null || command.counts().isEmpty()) {
            return Result.failure(ApplicationError.businessRuleViolation("At least one batch must be counted",
                    "inventory.errors.no-counts"));
        }
        for (var count : command.counts()) {
            if (!batches.existsById(count.batchId())) {
                return Result.failure(ApplicationError.notFound("Batch", count.batchId()));
            }
        }
        return Results.attempt(() -> {
            var mismatches = command.counts().stream()
                    .filter(count -> batches.findById(count.batchId()).orElseThrow().getCurrentQty() != count.countedQty())
                    .toList();
            var reconciliation = reconciliations.save(InventoryReconciliation.start(command.counts().size(),
                    command.counts().size() - mismatches.size(), command.userId()));
            for (var count : mismatches) {
                var batch = batches.findById(count.batchId()).orElseThrow();
                discrepancies.save(InventoryDiscrepancy.detect(reconciliation.getId(), batch.getId(),
                        batch.getCurrentQty(), count.countedQty()));
            }
            return reconciliation;
        });
    }

    public Result<InventoryReconciliation, ApplicationError> handle(ConfirmReconciliationCommand command) {
        var reconciliation = reconciliations.findById(command.reconciliationId());
        if (reconciliation.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Reconciliation", command.reconciliationId()));
        }
        return Results.attempt(() -> {
            reconciliation.get().confirm(command.userId(), discrepancies.countByReconciliationIdAndStatus(
                    command.reconciliationId(), DiscrepancyStatus.OPEN));
            return reconciliations.save(reconciliation.get());
        });
    }
}
