package com.codecraft.bevtrace.platform.analytics.application.internal.outboundservices.acl;

import com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects.OperationalData;
import com.codecraft.bevtrace.platform.dispatch.interfaces.acl.DispatchContextFacade;
import com.codecraft.bevtrace.platform.inventory.interfaces.acl.InventoryContextFacade;
import org.springframework.stereotype.Service;

/**
 * Collects the operational data of the dispatch and inventory contexts through their ACL facades.
 */
@Service
public class ExternalOperationalDataService {

    private final DispatchContextFacade dispatch;
    private final InventoryContextFacade inventory;

    public ExternalOperationalDataService(DispatchContextFacade dispatch, InventoryContextFacade inventory) {
        this.dispatch = dispatch;
        this.inventory = inventory;
    }

    public OperationalData fetchOperationalData() {
        return new OperationalData(
                dispatch.orders().stream().map(o -> new OperationalData.Order(o.delivered(), o.scheduledDate(),
                        o.deliveredAt(), o.requestedQuantity(), o.deliveredQuantity())).toList(),
                inventory.batches().stream().map(b -> new OperationalData.Batch(b.available(), b.currentQty(),
                        b.initialQty(), b.receivedAt())).toList(),
                inventory.waste().stream().map(w -> new OperationalData.Waste(w.reportedDate(), w.quantity())).toList(),
                inventory.reconciliations().stream().map(r -> new OperationalData.Reconciliation(r.date(),
                        r.reconciled(), r.eriPercentage())).toList());
    }
}
