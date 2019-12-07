package com.root.driver.report;

import com.root.driver.report.model.DriverReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for {@link CommandProcessor}
 */
class CommandProcessorTest {

    /**
     * Given A CSV File
     * When the csv file doesn't exist
     * Then {@link CommandProcessor#generateDriverReports(File)} throws a {@link FileNotFoundException}.
     */
    @Test
    void generateDriverReports_nonExistentFile() {
        Assertions.assertThrows(FileNotFoundException.class, () -> CommandProcessor.Factory.createInstance().generateDriverReports(
                new File("non_existent.csv")
        ));
    }

    /**
     * Given A CSV File
     * When the data in the csv file is fully valid
     * Then {@link CommandProcessor#generateDriverReports(File)} returns the driver reports.
     * @throws Exception If there's an exception reading the file.
     */
    @Test
    void generateDriverReports_fullyValidCSV() throws Exception {
        List<DriverReport> expectedDriverReports = Arrays.asList(
                new DriverReport("Megan", 90, 36),
                new DriverReport("Mark", 60, 54),
                new DriverReport("Ram", 60, 60),
                new DriverReport("Test", 56, 50),
                new DriverReport("Dan", 49, 25),
                new DriverReport("Stella", 42, 38),
                new DriverReport("Tan", 0, 0),
                new DriverReport("Gary", 0, 0)
        );
        List<DriverReport> actualDriverReports = CommandProcessor.Factory.createInstance().generateDriverReports(
                new File("./src/test/resources/fully_valid_data.csv")
        );
        assertEquals(expectedDriverReports.size(), actualDriverReports.size());
        for (int i = 0; i < expectedDriverReports.size(); i++) {
            assertDriverReports(expectedDriverReports.get(i), actualDriverReports.get(i));
        }
    }

    /**
     * Given A CSV File
     * When the data in the csv file is partially valid
     * Then {@link CommandProcessor#generateDriverReports(File)} returns the driver reports.
     *
     * Partially Valid:
     *   The csv contains
     *   1) Unrecognized commands
     *   2) Improper arguments
     *   3) Missing arguments
     *
     * @throws Exception If there's an exception reading the file.
     *
     */
    @Test
    void generateDriverReports_partiallyValidCSV() throws Exception {
        List<DriverReport> expectedDriverReports = Arrays.asList(
                new DriverReport("Megan", 90, 36),
                new DriverReport("Mark", 60, 54),
                new DriverReport("Test", 56, 50),
                new DriverReport("Stella", 42, 38),
                new DriverReport("Dan", 0, 0),
                new DriverReport("Tan", 0, 0),
                new DriverReport("Gary", 0, 0),
                new DriverReport("Ram", 0, 0)
        );
        List<DriverReport> actualDriverReports = CommandProcessor.Factory.createInstance().generateDriverReports(
                new File("./src/test/resources/partially_valid_data.csv")
        );
        assertEquals(expectedDriverReports.size(), actualDriverReports.size());
        for (int i = 0; i < expectedDriverReports.size(); i++) {
            assertDriverReports(expectedDriverReports.get(i), actualDriverReports.get(i));
        }
    }

    /**
     * Given A CSV File
     * When the data in the csv file is completely invalid with no parseable commands and values
     * Then {@link CommandProcessor#generateDriverReports(File)} returns an empty driver report.
     *
     * @throws Exception If there's an exception reading the file.
     *
     */
    @Test
    void generateDriverReports_fullyInvalidCSV() throws Exception {
        List<DriverReport> expectedDriverReports = Collections.emptyList();
        List<DriverReport> actualDriverReports = CommandProcessor.Factory.createInstance().generateDriverReports(
                new File("./src/test/resources/fully_invalid_data.csv")
        );
        assertEquals(expectedDriverReports.size(), actualDriverReports.size());
    }

    /**
     * Assert that two driver report objects are equal.
     * @param expected The expected driver report to compare to.
     * @param actual The actual driver report.
     */
    private void assertDriverReports(DriverReport expected, DriverReport actual) {
        assertEquals(expected.getDriver(), actual.getDriver());
        assertEquals(expected.getMilesDriven(), actual.getMilesDriven());
        assertEquals(expected.getAverageSpeed(), actual.getAverageSpeed());
    }

}
