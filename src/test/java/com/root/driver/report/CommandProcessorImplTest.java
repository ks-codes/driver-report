package com.root.driver.report;

import com.root.driver.report.exception.InvalidDataException;
import com.root.driver.report.exception.UnrecognizedCommandException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link CommandProcessorImpl}.
 */
class CommandProcessorImplTest {

    /**
     * Given a file location
     * When the file doesn't exist
     * Then generateDriverReports should throw an {@link IOException}.
     */
    @Test
    void generateDriverReports_throwsIOException() {
        assertThrows(IOException.class,
                () ->  new CommandProcessorImpl().generateDriverReports(new File("data.csv")));
    }

    /**
     * Given a string of values
     * When the command is not recognized
     * Then processCSVLine should throw an {@link UnrecognizedCommandException}.
     */
    @ParameterizedTest
    @ValueSource(strings = {"XYZ,Dan,1,2,3", " , , ,"})
    void processCSVLine_throws_UnrecognizedCommandException(String line) {
        Throwable exception = assertThrows(UnrecognizedCommandException.class,
                () ->  new CommandProcessorImpl().processCSVLine(line));
        assertEquals("Unknown command encountered!", exception.getMessage());
    }

    /**
     * Given a string of values
     * When the start time or end time cannot be parsed
     * Then processCSVLine should throw an {@link DateTimeParseException}.
     */
    @ParameterizedTest
    @ValueSource(strings = {"Trip,Dan,01:15,x,3", "Trip,Dan,1,2,3", "Trip,Dan,1:15,2:15,3"})
    void processCSVLine_throws_DateTimeParseException(String line) {
       assertThrows(DateTimeParseException.class,
                () ->  new CommandProcessorImpl().processCSVLine(line));
    }

    /**
     * Given a string of values
     * When the data is incomplete or null
     * Then processCSVLine should throw an {@link InvalidDataException}.
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "Driver",
            "Trip",
            "Driver,",
            "XYZ",
            "",
            "    ",
            "Trip,,    ,,",
            "Trip,Dan,01:15",
            "Trip,Dan,01:15,02:00",
            "Trip,,01:15,02:15,3"
    })
    void processCSVLine_throws_InvalidDataException(String line) {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> new CommandProcessorImpl().processCSVLine(line));
        assertEquals("Invalid Row:" + line, exception.getMessage());
    }

    /**
     * Given a string of values
     * When the data is valid
     * Then processCSVLine should not throw an exception.
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "Driver,Dan",
            "Trip,Dan,01:15,02:15,40",
            "Trip,Dan,01:15,02:15,1"
    })
    void processCSVLine_success(String line) {
        // Since processCSVLine doesn't return anything, there's nothing to assert.
        // Only thing to verify is that the execution happens without exception.
        new CommandProcessorImpl().processCSVLine(line);
    }
}
