package com.codecraft.bevtrace.platform.dispatch.domain.services;

/**
 * Weight and pallet conventions used to size a cargo.
 */
public final class CargoPolicy {

    public static final double KG_PER_UNIT = 1.1;
    public static final int UNITS_PER_PALLET = 400;

    private CargoPolicy() {
    }

    /**
     * @param units number of units
     * @return weight in kilograms rounded to one decimal
     */
    public static double weightOf(int units) {
        return Math.round(units * KG_PER_UNIT * 10) / 10.0;
    }

    /**
     * @param units number of units
     * @return pallets needed, at least one
     */
    public static int palletsOf(int units) {
        return Math.max(1, (int) Math.ceil((double) units / UNITS_PER_PALLET));
    }
}
