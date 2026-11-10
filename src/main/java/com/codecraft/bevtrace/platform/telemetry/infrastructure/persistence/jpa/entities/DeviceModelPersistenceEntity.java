package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.DeviceModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Stored form of {@link DeviceModel}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "device_models")
public class DeviceModelPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String name;
    private String manufacturer;
    private String sensors;

    public static DeviceModelPersistenceEntity fromDomain(DeviceModel d) {
        var e = new DeviceModelPersistenceEntity();
        e.setId(d.getId());
        e.name = d.getName();
        e.manufacturer = d.getManufacturer();
        e.sensors = d.getSensors();
        return e;
    }

    public DeviceModel toDomain() {
        return DeviceModel.restore(getId(), name, manufacturer, sensors);
    }
}
