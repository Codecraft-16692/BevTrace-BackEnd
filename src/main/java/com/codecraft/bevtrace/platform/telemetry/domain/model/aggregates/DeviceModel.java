package com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

/**
 * A hardware model of IoT tracker (reference data).
 */
public class DeviceModel extends AbstractDomainAggregateRoot<DeviceModel> {

    private Long id;
    private String name;
    private String manufacturer;
    private String sensors;

    protected DeviceModel() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getSensors() {
        return sensors;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static DeviceModel restore(Long id, String name, String manufacturer, String sensors) {
        var a = new DeviceModel();
        a.id = id;
        a.name = name;
        a.manufacturer = manufacturer;
        a.sensors = sensors;
        return a;
    }
}
