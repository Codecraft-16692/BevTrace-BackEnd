package com.codecraft.bevtrace.platform.traceability.domain.services;

import java.time.Duration;
import java.util.List;

/**
 * Plans the planned route of a shipment: departure from the central warehouse and the destination.
 * The travel time is an estimate from the straight-line distance; real positions come from telemetry.
 */
public final class RoutePlanner {

    public static final String ORIGIN_NAME = "BevTrace Central Warehouse - Ate";
    public static final double ORIGIN_LATITUDE = -12.0433;
    public static final double ORIGIN_LONGITUDE = -76.942;
    private static final double AVERAGE_SPEED_KMH = 28;
    private static final double EARTH_RADIUS_KM = 6371;

    /**
     * A planned stop.
     *
     * @param sequence     order in the route, starting at 1
     * @param locationName place name
     * @param latitude     latitude
     * @param longitude    longitude
     */
    public record Point(int sequence, String locationName, double latitude, double longitude) {
    }

    private RoutePlanner() {
    }

    /**
     * @param destinationName name of the destination
     * @param latitude        destination latitude
     * @param longitude       destination longitude
     * @return departure and destination points
     */
    public static List<Point> plan(String destinationName, double latitude, double longitude) {
        return List.of(
                new Point(1, ORIGIN_NAME + " - Departure", ORIGIN_LATITUDE, ORIGIN_LONGITUDE),
                new Point(2, destinationName, latitude, longitude));
    }

    /**
     * @param latitude  destination latitude
     * @param longitude destination longitude
     * @return estimated travel time, at least one hour (loading and traffic included)
     */
    public static Duration estimateTravel(double latitude, double longitude) {
        double dLat = Math.toRadians(latitude - ORIGIN_LATITUDE);
        double dLng = Math.toRadians(longitude - ORIGIN_LONGITUDE);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(ORIGIN_LATITUDE)) * Math.cos(Math.toRadians(latitude))
                * Math.pow(Math.sin(dLng / 2), 2);
        double km = EARTH_RADIUS_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        long minutes = Math.max(60, Math.round(km / AVERAGE_SPEED_KMH * 60) + 30);
        return Duration.ofMinutes(minutes);
    }
}
