package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.WarehouseZone;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Stored form of {@link WarehouseZone}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "warehouse_zones")
public class WarehouseZonePersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String name;
    private String aisle;
    private String rack;

    public static WarehouseZonePersistenceEntity fromDomain(WarehouseZone d) {
        var e = new WarehouseZonePersistenceEntity();
        e.setId(d.getId());
        e.name = d.getName();
        e.aisle = d.getAisle();
        e.rack = d.getRack();
        return e;
    }

    public WarehouseZone toDomain() {
        return WarehouseZone.restore(getId(), name, aisle, rack);
    }
}
