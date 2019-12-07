package com.root.driver.report.model;

/**
 * Class that represents the report to be generated per Driver.
 */
public class DriverReport {
    private final String driver;
    private final int milesDriven;
    private final int averageSpeed;

    // Getters
    public String getDriver() {
        return driver;
    }

    public int getMilesDriven() {
        return milesDriven;
    }

    public int getAverageSpeed() {
        return averageSpeed;
    }

    /**
     * Constructor
     * @param driver The name of the driver
     * @param milesDriven The total miles driven
     * @param averageSpeed The average speed of the driver
     */
    public DriverReport(String driver, int milesDriven, int averageSpeed) {
        this.driver = driver;
        this.milesDriven = milesDriven;
        this.averageSpeed = averageSpeed;
    }
}