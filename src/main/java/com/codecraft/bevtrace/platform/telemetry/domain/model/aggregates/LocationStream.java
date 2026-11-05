package com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates;

import com.codecraft.bevtrace.platform.telemetry.domain.model.events.ReadingRecorded;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * One reading emitted by a device: position, speed and cargo temperature.
 */
public class LocationStream extends AbstractDomainAggregateRoot<LocationStream> {

    private Long id;
    private Long deviceId;
    private double latitude;
    private double longitude;
    private double speed;
    private double temperature;
    private Instant timestamp;

    protected LocationStream() {
    }

    /**
     * Records a reading and announces it to the other contexts.
     *
     * @param deviceId    emitting device
     * @param latitude    latitude
     * @param longitude   longitude
     * @param speed       speed in km/h
     * @param temperature cargo temperature in Celsius
     * @param timestamp   measurement time
     * @return the reading
     */
    public static LocationStream record(Long deviceId, double latitude, double longitude, double speed,
                                        double temperature, Instant timestamp) {
        var stream = new LocationStream();
        stream.deviceId = deviceId;
        stream.latitude = latitude;
        stream.longitude = longitude;
        stream.speed = speed;
        stream.temperature = temperature;
        stream.timestamp = timestamp;
        stream.registerDomainEvent(new ReadingRecorded(deviceId, latitude, longitude, temperature, timestamp));
        return stream;
    }

    public double getTemperature() {
        return temperature;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getSpeed() {
        return speed;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static LocationStream restore(Long id, Long deviceId, double latitude, double longitude, double speed, double temperature, Instant timestamp) {
        var a = new LocationStream();
        a.id = id;
        a.deviceId = deviceId;
        a.latitude = latitude;
        a.longitude = longitude;
        a.speed = speed;
        a.temperature = temperature;
        a.timestamp = timestamp;
        return a;
    }
}
