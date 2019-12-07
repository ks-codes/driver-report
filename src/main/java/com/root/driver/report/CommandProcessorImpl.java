package com.root.driver.report;

import com.root.driver.report.exception.InvalidDataException;
import com.root.driver.report.exception.UnrecognizedCommandException;
import com.root.driver.report.model.Command;
import com.root.driver.report.model.DriverReport;
import com.root.driver.report.model.MinutesAndMiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to handle processing the input CSV file.
 */
public final class CommandProcessorImpl extends BaseCommandProcessorImpl implements CommandProcessor {
    private static final Logger LOGGER = Logger.getLogger(CommandProcessorImpl.class.getName());

    // Key: Driver name
    private final Map<String, MinutesAndMiles> driverTrips = new HashMap<>();
    private static final int COMMAND_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int START_TIME_INDEX = 2;
    private static final int END_TIME_INDEX = 3;
    private static final int MILES_DRIVEN_INDEX = 4;

    /**
     * {@inheritDoc}
     * @apiNote In the event that other exceptions occur due to invalid data,
     *          the specific line will be ignored and the report generation will move on to
     *          the next line.
     */
    @Override
    public List<DriverReport> generateDriverReports(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    processCSVLine(line);
                } catch (UnrecognizedCommandException e) {
                    LOGGER.log(Level.WARNING, "Command not recognized.");
                } catch (InvalidDataException e) {
                    LOGGER.log(Level.WARNING, "The data row is incomplete or invalid.");
                } catch (DateTimeParseException e) {
                    LOGGER.log(Level.WARNING, "Time could not be parsed.");
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Miles driven could not be parsed.");
                }
            }
            return sortByMilesDrivenDesc(consolidateReports());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "The file does not exist or could not be read.");
            throw e;
        }
    }

    /**
     * Processes each line of the given comma separated line.
     * @param line A comma separated line containing a command and corresponding metrics.
     */
    final void processCSVLine(String line) throws UnrecognizedCommandException {
        // Split the line by comma.
        String[] values = line.split(",");

        // The minimum expected length is 2 since Driver command has one argument. i.e., Driver,Mark
        if(values.length < 2)
            throw new InvalidDataException("Invalid Row:" + line);

        Command command;
        try{
            // Enum.valueOf throws IllegalArgumentException if the command is not recognized.
            // The IllegalArgumentException is caught and rethrown as an UnrecognizedCommandException for an easier read.
            command = Enum.valueOf(Command.class, values[COMMAND_INDEX].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnrecognizedCommandException("Unknown command encountered!", e);
        }
        switch(command) {
            case DRIVER:
                registerDriver(values[NAME_INDEX].trim());
                // If there are other values beyond index 1, they are ignored.
                break;
            case TRIP:
                // Name, start time,end time and miles driven are required.
                if(!areTripArgsValid(values))
                    throw new InvalidDataException("Invalid Row:" + line);

                String name = values[NAME_INDEX];
                LocalTime startTime = LocalTime.parse(values[START_TIME_INDEX]);
                LocalTime endTime = LocalTime.parse(values[END_TIME_INDEX]);

                // Only if the end time is after start time, we consider it a valid row.

                if (endTime.isAfter(startTime)) {
                    double milesDriven = Double.parseDouble(values[MILES_DRIVEN_INDEX]);
                    int minutes = (int) Duration.between(startTime, endTime).toMinutes();
                    double speed = getSpeed(milesDriven, minutes);

                    // Only if MIN_SPEED <= speed <= MAX_SPEED, add to the driver's profile.
                    if (isSpeedInRange(speed)) {
                        addTrip(name, minutes, milesDriven);
                    }
                }
                break;
            default:
                // It's impossible to get to the default block as Enum.valueOf() will
                // throw an IllegalArgumentException in case of an unrecognized command.
        }
    }

    /**
     * Verifies that all values required to process TRIP command exist.
     * @param values The comma separated values.
     * @return true or false indicating validity.
     */
    private static boolean areTripArgsValid(String[] values) {
        if(MILES_DRIVEN_INDEX < values.length) {
            boolean isValid = true;
            for(int i = NAME_INDEX; i <= MILES_DRIVEN_INDEX; i++) {
                isValid = isValid && isStringBlank(values[i]);
            }
            return isValid;
        }
        return false;
    }

    /**
     * Returns if the string is null,empty or blank.
     * @param value The string value
     * @return true or false.
     */
    private static boolean isStringBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Consolidates the reports of each driver to the rounded off speeds and distances.
     * @return The collection of reports for each driver.
     */
    private List<DriverReport> consolidateReports() {
        List<DriverReport> reports = new ArrayList<>();
        for(String driver: driverTrips.keySet()) {
            MinutesAndMiles minsAndMiles = driverTrips.get(driver);
            int roundedOffSpeed = (int) Math.round(getSpeed(minsAndMiles.getMiles(), minsAndMiles.getMinutes()));
            int roundedOffDistance = (int) Math.round(minsAndMiles.getMiles());

            reports.add(new DriverReport(driver, roundedOffDistance, roundedOffSpeed));
        }
        return reports;
    }

    /**
     * Creates a profile for the driver.
     * @param name Name of the driver
     */
    private void registerDriver(String name) {
        addTrip(name, 0, 0);
    }

    /**
     * Adds the distance and minutes driven to a driver's profile.
     * @param name Name of the driver.
     * @param minutes The minutes driven.
     * @param distance The distance driven.
     */
    private void addTrip(String name, int minutes, double distance) {
        MinutesAndMiles minsAndMiles = driverTrips.get(name);
        if(minsAndMiles == null) {
            driverTrips.put(name, new MinutesAndMiles(distance, minutes));
        } else {
            driverTrips.put(name,
                    new MinutesAndMiles(minsAndMiles.getMiles() + distance, minsAndMiles.getMinutes() + minutes));
        }
    }
}
