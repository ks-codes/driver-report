package com.root.driver.report.executor;

import com.root.driver.report.CommandProcessor;
import com.root.driver.report.model.DriverReport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to execute the
 */
public class Executor {
    private static final Logger LOGGER = Logger.getLogger(Executor.class.getName());
    public static void main(String[] args) {
        File file = new File("./src/main/resources/test/fully_valid_data.csv");
//        File file = new File("./src/main/resources/test/partially_valid_data.csv");
//        File file = new File("./src/main/resources/test/fully_invalid_data.csv");
        try {
            List<DriverReport> driverReports = CommandProcessor.Factory.createInstance().generateDriverReports(file);
            for(DriverReport r: driverReports) {
                LOGGER.log(Level.INFO, r.getDriver() + ":"
                        + r.getMilesDriven() + "@" + r.getAverageSpeed());
            }
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "File not found; no report generated.");
        }
    }
}
