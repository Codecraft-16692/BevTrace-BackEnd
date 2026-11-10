package com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates;

import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.DataStatus;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * An interval in which a device did not report.
 */
public class DisconnectionPeriod extends AbstractDomainAggregateRoot<DisconnectionPeriod> {

    private Long id;
    private Long deviceId;
    private Instant startedAt;
    private Instant endedAt;
    private DataStatus dataStatus;

    protected DisconnectionPeriod() {
    }

    /**
     * Opens a period.
     *
     * @param deviceId  device that stopped reporting
     * @param startedAt time of the last signal
     * @return an open period
     */
    public static DisconnectionPeriod open(Long deviceId, Instant startedAt) {
        var period = new DisconnectionPeriod();
        period.deviceId = deviceId;
        period.startedAt = startedAt;
        period.dataStatus = DataStatus.AVAILABLE;
        return period;
    }

    /**
     * Closes the period when the device reports again.
     *
     * @param at time of the first signal after the gap
     */
    public void close(Instant at) {
        endedAt = at;
    }

    public Long getId() {
        return id;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public DataStatus getDataStatus() {
        return dataStatus;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static DisconnectionPeriod restore(Long id, Long deviceId, Instant startedAt, Instant endedAt, DataStatus dataStatus) {
        var a = new DisconnectionPeriod();
        a.id = id;
        a.deviceId = deviceId;
        a.startedAt = startedAt;
        a.endedAt = endedAt;
        a.dataStatus = dataStatus;
        return a;
    }
}
