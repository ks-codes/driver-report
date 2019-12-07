package com.root.driver.report.model;

/**
 * Class that's useful to collect a driver's information.
 */
public class MinutesAndMiles {
    private final double miles;
    private final int minutes;

    public MinutesAndMiles(double miles, int minutes) {
        this.miles = miles;
        this.minutes = minutes;
    }

    // Getters
    public double getMiles() {
        return miles;
    }

    public int getMinutes() {
        return minutes;
    }
}
