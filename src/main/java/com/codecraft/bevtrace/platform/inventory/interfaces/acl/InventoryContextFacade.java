package com.codecraft.bevtrace.platform.inventory.interfaces.acl;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.ProductBatch;
import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.BatchStatus;
import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.ReconciliationStatus;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.InventoryReconciliationRepository;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductBatchRepository;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.WasteRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The only door other contexts use to ask the inventory context about stock.
 */
@Service
@Transactional(readOnly = true)
public class InventoryContextFacade {

    /** Stock view of a batch. */
    public record BatchView(Long id, String batchNumber, boolean available, int currentQty, int initialQty,
                            Instant receivedAt) {
    }

    /** A waste, as needed by the indicators. */
    public record WasteView(LocalDate reportedDate, int quantity) {
    }

    /** A reconciliation, as needed by the indicators. */
    public record ReconciliationView(LocalDate date, boolean reconciled, double eriPercentage) {
    }

    private static final Map<String, List<String>> ALL = Map.of();

    private final ProductBatchRepository batches;
    private final WasteRecordRepository waste;
    private final InventoryReconciliationRepository reconciliations;

    public InventoryContextFacade(ProductBatchRepository batches, WasteRecordRepository waste,
                                  InventoryReconciliationRepository reconciliations) {
        this.batches = batches;
        this.waste = waste;
        this.reconciliations = reconciliations;
    }

    public Optional<BatchView> batch(Long batchId) {
        return batches.findById(batchId).map(InventoryContextFacade::view);
    }

    public List<BatchView> batches() {
        return batches.search(ALL, null, null, null).stream().map(InventoryContextFacade::view).toList();
    }

    public List<WasteView> waste() {
        return waste.search(ALL, null, null, null).stream()
                .map(w -> new WasteView(w.getReportedDate(), w.getQuantity())).toList();
    }

    public List<ReconciliationView> reconciliations() {
        return reconciliations.search(ALL, null, null, null).stream()
                .map(r -> new ReconciliationView(r.getDate(), r.getStatus() == ReconciliationStatus.RECONCILED,
                        r.getEriPercentage()))
                .toList();
    }

    private static BatchView view(ProductBatch batch) {
        return new BatchView(batch.getId(), batch.getBatchNumber(), batch.getStatus() == BatchStatus.AVAILABLE,
                batch.getCurrentQty(), batch.getInitialQty(), batch.getReceivedAt());
    }
}
