package com.root.driver.report.executor;

import com.root.driver.report.CommandProcessor;
import com.root.driver.report.model.DriverReport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to execute the CommandProcessor functionality.
 */
public class Executor {
    private static final Logger LOGGER = Logger.getLogger(Executor.class.getName());
    public static void main(String[] args) {
        File file = new File(args[0]);
        try {
            List<DriverReport> driverReports = CommandProcessor.Factory.createInstance().generateDriverReports(file);
            for(DriverReport r: driverReports) {
                System.out.println(r.getDriver() + ":"
                        + r.getMilesDriven() + " miles @ " + r.getAverageSpeed() + " mph");
            }
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "File not found; no report generated.");
        }
    }
}
