package com.codecraft.bevtrace.platform.traceability.domain.model.aggregates;

import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.CheckpointStatus;
import com.codecraft.bevtrace.platform.traceability.domain.services.RoutePlanner;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * A planned point of the route of a log.
 */
public class RouteCheckpoint extends AbstractDomainAggregateRoot<RouteCheckpoint> {

    private Long id;
    private Long logId;
    private int sequence;
    private String locationName;
    private double latitude;
    private double longitude;
    private CheckpointStatus status;
    private Instant reachedAt;
    private String observation;

    protected RouteCheckpoint() {
    }

    /**
     * Creates a planned point.
     *
     * @param logId    owning log
     * @param point    planned point
     * @param reachedAt time it was reached, null for a pending point
     * @return the checkpoint
     */
    public static RouteCheckpoint plan(Long logId, RoutePlanner.Point point, Instant reachedAt) {
        var checkpoint = new RouteCheckpoint();
        checkpoint.logId = logId;
        checkpoint.sequence = point.sequence();
        checkpoint.locationName = point.locationName();
        checkpoint.latitude = point.latitude();
        checkpoint.longitude = point.longitude();
        checkpoint.status = reachedAt == null ? CheckpointStatus.PENDING : CheckpointStatus.REACHED;
        checkpoint.reachedAt = reachedAt;
        return checkpoint;
    }

    /**
     * Marks the point as reached (no effect when it already was).
     *
     * @param at time reached
     */
    public void reach(Instant at) {
        if (status == CheckpointStatus.REACHED) {
            return;
        }
        status = CheckpointStatus.REACHED;
        reachedAt = at;
    }

    public Long getId() {
        return id;
    }

    public Long getLogId() {
        return logId;
    }

    public int getSequence() {
        return sequence;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public CheckpointStatus getStatus() {
        return status;
    }

    public Instant getReachedAt() {
        return reachedAt;
    }

    public String getObservation() {
        return observation;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static RouteCheckpoint restore(Long id, Long logId, int sequence, String locationName, double latitude, double longitude, CheckpointStatus status, Instant reachedAt, String observation) {
        var a = new RouteCheckpoint();
        a.id = id;
        a.logId = logId;
        a.sequence = sequence;
        a.locationName = locationName;
        a.latitude = latitude;
        a.longitude = longitude;
        a.status = status;
        a.reachedAt = reachedAt;
        a.observation = observation;
        return a;
    }
}
