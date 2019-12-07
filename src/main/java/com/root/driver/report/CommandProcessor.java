package com.root.driver.report;

import com.root.driver.report.model.DriverReport;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * An interface to process the command.
 */
public interface CommandProcessor {
    /**
     * Processes the input file and returns a list of {@link DriverReport}s.
     * @return A collection of {@link DriverReport}s.
     * @throws IOException If the file is invalid or could not be read.
     */
    List<DriverReport> generateDriverReports(File file) throws IOException;

    /**
     * Factory to createInstance an implementation
     */
    class Factory {
        public static CommandProcessor createInstance() {
            return new CommandProcessorImpl();
        }
    }
}
