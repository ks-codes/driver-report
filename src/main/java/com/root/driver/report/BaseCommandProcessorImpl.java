package com.root.driver.report;

import com.root.driver.report.model.DriverReport;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * An abstract implementation to process input.
 */
abstract class BaseCommandProcessorImpl implements CommandProcessor {
    private static final double MIN_SPEED = 5;
    private static final double MAX_SPEED = 100;

    public abstract List<DriverReport> generateDriverReports(File file) throws IOException;

    /**
     * Returns if the speed is in the expected range.
     * @param speed The speed to check range for.
     * @return true or false.
     */
    static boolean isSpeedInRange(double speed) {
        return MIN_SPEED <= speed && speed <= MAX_SPEED;
    }

    /**
     * Returns the speed rounded off to the closest integer.
     * @param distance The total distance driven.
     * @param durationInMinutes The total time driven (in Minutes).
     * @return The speed as an integer.
     */
    static double getSpeed(double distance, int durationInMinutes) {

        // If the time driven is zero, return zero since there's no speed to calculate.

        // Reason to not throw an exception: If there's a driver registered but has no trips, then their distance would be
        // zero with zero minutes driven. Hence, it makes logical sense to call it a zero speed
        // rather than throw an exception (which is semantically correct here).
        if(durationInMinutes == 0) return 0;

        double timeInHours = (double) durationInMinutes/60;
        return distance/timeInHours;
    }

    /**
     * Sorts the reports in descending order
     * @param reports The collection of reports.
     * @return The collection of reports sorted by the distance driven in descending order.
     */
    static List<DriverReport> sortByMilesDrivenDesc(List<DriverReport> reports) {
        if(reports == null) return Collections.emptyList();
        // reverseOrder() here is important as we want the sorting to be done in descending order.
        reports.sort(Collections.reverseOrder(
                (report1, report2) -> {
                    if(report1.getMilesDriven() < report2.getMilesDriven()) return -1;
                    if(report1.getMilesDriven() > report2.getMilesDriven()) return 1;
                    return 0;
                }
        ));
        return reports;
    }
}
