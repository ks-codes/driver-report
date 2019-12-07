package com.root.driver.report.exception;

/**
 * Exception to be thrown when an malformed data row is encountered.
 */
public class InvalidDataException extends IllegalArgumentException {
    /**
     * Constructs an InvalidDataException with the
     * specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidDataException(String message) {
        super(message);
    }
}
