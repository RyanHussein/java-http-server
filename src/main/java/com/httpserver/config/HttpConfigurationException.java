package com.httpserver.config;

/**
 * Exception thrown when there is a configuration error in the HTTP server.
 */
public class HttpConfigurationException extends RuntimeException {

    /**
     * Constructs a new HttpConfigurationException with the specified detail message.
     *
     * @param message the detail message
     */
    public HttpConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new HttpConfigurationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public HttpConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
