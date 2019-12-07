package com.root.driver.report.exception;

/**
 * Exception to be thrown when an unrecognized command is encountered.
 */
public class UnrecognizedCommandException extends IllegalArgumentException {
    /**
     * Constructs an UnrecognizedCommandException with the
     * specified detail message.
     *
     * @param message The detail message.
     * @param cause The cause indicating the root cause of the exception.
     */
    public UnrecognizedCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
