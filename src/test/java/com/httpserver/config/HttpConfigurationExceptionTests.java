package com.httpserver.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HttpConfigurationException class.
 */
public class HttpConfigurationExceptionTests {

    @Test
    void testHttpConfigurationExceptionWithMessage() {
        String message = "Configuration error";
        HttpConfigurationException exception = new HttpConfigurationException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testHttpConfigurationExceptionWithMessageAndCause() {
        String message = "Configuration error";
        Throwable cause = new RuntimeException("Cause");
        HttpConfigurationException exception = new HttpConfigurationException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}