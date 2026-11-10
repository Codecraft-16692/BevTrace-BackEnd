package com.codecraft.bevtrace.platform.inventory.application.commandservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.WasteRecord;
import com.codecraft.bevtrace.platform.inventory.domain.model.commands.ReportWasteCommand;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductBatchRepository;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.WasteRecordRepository;
import com.codecraft.bevtrace.platform.shared.application.result.ApplicationError;
import com.codecraft.bevtrace.platform.shared.application.result.Result;
import com.codecraft.bevtrace.platform.shared.application.result.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Commands on waste records: reporting a waste deducts it from the batch stock.
 */
@Service
@Transactional
public class WasteRecordCommandService {

    private final WasteRecordRepository waste;
    private final ProductBatchRepository batches;

    public WasteRecordCommandService(WasteRecordRepository waste, ProductBatchRepository batches) {
        this.waste = waste;
        this.batches = batches;
    }

    public Result<WasteRecord, ApplicationError> handle(ReportWasteCommand command) {
        var batch = batches.findById(command.batchId());
        if (batch.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Batch", command.batchId()));
        }
        return Results.attempt(() -> {
            var record = WasteRecord.report(command.batchId(), command.userId(), command.quantity(), command.reason());
            batch.get().deduct(command.quantity());
            batches.save(batch.get());
            return waste.save(record);
        });
    }
}
