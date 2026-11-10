package com.codecraft.bevtrace.platform.inventory.application.commandservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryDiscrepancy;
import com.codecraft.bevtrace.platform.inventory.domain.model.commands.ResolveDiscrepancyCommand;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.InventoryDiscrepancyRepository;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductBatchRepository;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Commands on discrepancies: resolving one aligns the batch stock with the count.
 */
@Service
@Transactional
public class InventoryDiscrepancyCommandService {

    private final InventoryDiscrepancyRepository discrepancies;
    private final ProductBatchRepository batches;

    public InventoryDiscrepancyCommandService(InventoryDiscrepancyRepository discrepancies,
                                              ProductBatchRepository batches) {
        this.discrepancies = discrepancies;
        this.batches = batches;
    }

    public Result<InventoryDiscrepancy, ApplicationError> handle(ResolveDiscrepancyCommand command) {
        var discrepancy = discrepancies.findById(command.discrepancyId());
        if (discrepancy.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Discrepancy", command.discrepancyId()));
        }
        var batch = batches.findById(discrepancy.get().getBatchId());
        if (batch.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Batch", discrepancy.get().getBatchId()));
        }
        return Results.attempt(() -> {
            discrepancy.get().resolve(command.note());
            batch.get().adjustTo(discrepancy.get().getCountedQty());
            batches.save(batch.get());
            return discrepancies.save(discrepancy.get());
        });
    }
}
