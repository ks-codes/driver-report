package com.root.driver.report;

import com.root.driver.report.model.DriverReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.root.driver.report.BaseCommandProcessorImpl.getSpeed;
import static com.root.driver.report.BaseCommandProcessorImpl.isSpeedInRange;
import static com.root.driver.report.BaseCommandProcessorImpl.sortByMilesDrivenDesc;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseCommandProcessorImplTest {
    /**
     * Given a speed
     * When the speed is in range
     * Then isSpeedInRange must return true.
     * @param speed The speed to test.
     */
    @ParameterizedTest
    @ValueSource(doubles = { 5, 5.0, 6.8, 100.0, 99.99999999999999999999, 100.00000000000000001})
    public void isSpeedInRange_returnsTrue(double speed) {
        assertTrue(isSpeedInRange(speed));
    }

    /**
     * Given a speed
     * When the speed is not in range
     * Then isSpeedInRange must return false.
     * @param speed The speed to test.
     */
    @ParameterizedTest
    @ValueSource(doubles = { 4.9999999, 150, 100.00000000000001 })
    public void isSpeedInRange_returnsFalse(double speed) {
        assertFalse(isSpeedInRange(speed));
    }

    /**
     * Given a distance and duration
     * When the roundOff is set to false
     * Then getSpeed must return the accurate decimal speed.
     * @param distance The distance to test.
     * @param durationInMinutes The duration.
     * @param expectedValue The expected output value.
     */
    @ParameterizedTest
    @CsvSource({ "100, 60, 100", "100, 49, 122.44897959183673"})
    public void getSpeed_returnsAccurateSpeed(double distance, int durationInMinutes, double expectedValue) {
        assertEquals(expectedValue, getSpeed(distance, durationInMinutes));
    }

    /**
     * Given a distance and duration
     * When the duration is zero
     * Then getSpeed must return zero.
     * @param distance The distance to test.
     * @param durationInMinutes The duration.
     * @param expectedValue The expected output value.
     */
    @ParameterizedTest
    @CsvSource({ "100, 0, 0", "0, 0, 0"})
    public void getSpeed_throwsArithmeticException(double distance, int durationInMinutes, double expectedValue) {
        assertEquals(expectedValue, getSpeed(distance, durationInMinutes));
    }

    /**
     * Given a list of driver reports
     * When the list is null
     * Then sortByMilesDrivenDesc must return an empty list.
     */
    @Test
    public void sortByMilesDrivenDesc_nullList() {
        List<DriverReport> reports = sortByMilesDrivenDesc(null);
        assertTrue(reports.isEmpty());
    }

    /**
     * Given a list of driver reports
     * When the list is empty
     * Then sortByMilesDrivenDesc must return an empty list.
     */
    @Test
    public void sortByMilesDrivenDesc_emptyList() {
        List<DriverReport> reports = sortByMilesDrivenDesc(Collections.emptyList());
        assertTrue(reports.isEmpty());
    }

    /**
     * Given a list of driver reports
     * When the list has one item
     * Then sortByMilesDrivenDesc must return a list with one item.
     */
    @Test
    public void sortByMilesDrivenDesc_listWithOneItem() {
        List<DriverReport> singleItem = singletonList(
                new DriverReport("Dan", 24, 40)
        );
        List<DriverReport> sortedReports = sortByMilesDrivenDesc(singleItem);
        assertEquals(singleItem.size(), sortedReports.size());
        assertEquals(singleItem.get(0).getMilesDriven(), sortedReports.get(0).getMilesDriven());
    }

    /**
     * Given a list of driver reports
     * When the list has multiple items
     * Then sortByMilesDrivenDesc must return a list sorted by the miles driven.
     */
    @Test
    public void sortByMilesDrivenDesc_listWithMultipleItems() {
        List<DriverReport> driverReports = asList(
                new DriverReport("Dan", 24, 40),
                new DriverReport("Ram", 48, 40),
                new DriverReport("Mat", 52, 60),
                new DriverReport("Pat", 2, 35)
        );
        // The sorting algorithm sorts the list in-place. Hence retaining a copy.
        List<DriverReport> unsortedReports = new ArrayList<>(driverReports);

        List<DriverReport> sortedReports = sortByMilesDrivenDesc(driverReports);
        assertEquals(driverReports.size(), sortedReports.size());
        // The sorted index order: Mat,Ram,Dan,Pat
        // unsortedReports.get(2) = sortedReports.get(0)
        // unsortedReports.get(1) = sortedReports.get(1)
        // unsortedReports.get(0) = sortedReports.get(2)
        // unsortedReports.get(3) = sortedReports.get(3)
        assertDriverReports(unsortedReports.get(2), sortedReports.get(0));
        assertDriverReports(unsortedReports.get(1), sortedReports.get(1));
        assertDriverReports(unsortedReports.get(0), sortedReports.get(2));
        assertDriverReports(unsortedReports.get(3), sortedReports.get(3));
    }

    /**
     * Given a list of driver reports
     * When the list has multiple items
     * Then sortByMilesDrivenDesc must return a list sorted by the miles driven.
     */
    @Test
    public void sortByMilesDrivenDesc_listWithEqualItems() {
        List<DriverReport> driverReports = asList(
                new DriverReport("Dan", 24, 40),
                new DriverReport("Ram", 24, 40),
                new DriverReport("Mat", 52, 60),
                new DriverReport("Pat", 2, 35)
        );
        // The sorting algorithm sorts the list in-place. Hence retaining a copy.
        List<DriverReport> unsortedReports = new ArrayList<>(driverReports);

        List<DriverReport> sortedReports = sortByMilesDrivenDesc(driverReports);
        assertEquals(driverReports.size(), sortedReports.size());
        // The sorted index order: Mat,Dan,Ram,Pat
        // unsortedReports.get(2) = sortedReports.get(0)
        // unsortedReports.get(0) = sortedReports.get(1)
        // unsortedReports.get(1) = sortedReports.get(2)
        // unsortedReports.get(3) = sortedReports.get(3)
        assertDriverReports(unsortedReports.get(2), sortedReports.get(0));
        assertDriverReports(unsortedReports.get(0), sortedReports.get(1));
        assertDriverReports(unsortedReports.get(1), sortedReports.get(2));
        assertDriverReports(unsortedReports.get(3), sortedReports.get(3));
    }

    /**
     * Asserts that two driver reports have the same values.
     * @param expected The expected report.
     * @param actual The actual report.
     */
    private void assertDriverReports(DriverReport expected, DriverReport actual) {
        assertEquals(expected.getDriver(), actual.getDriver());
        assertEquals(expected.getMilesDriven(), actual.getMilesDriven());
        assertEquals(expected.getAverageSpeed(), actual.getAverageSpeed());
    }
}
