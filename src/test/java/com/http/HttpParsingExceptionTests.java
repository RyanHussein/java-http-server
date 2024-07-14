package com.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HttpParsingException class.
 */
public class HttpParsingExceptionTests {

    @Test
    void testHttpParsingExceptionWithErrorCode() {
        HttpStatusCode errorCode = HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST;
        HttpParsingException exception = new HttpParsingException(errorCode);

        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(errorCode.MESSAGE, exception.getMessage());
    }

    @Test
    void testHttpParsingExceptionWithNullErrorCode() {
        HttpParsingException exception = new HttpParsingException(null);

        assertNull(exception.getErrorCode());
        assertNull(exception.getMessage());
    }

    @Test
    void testHttpParsingExceptionWithErrorCodeAndCustomMessage() {
        HttpStatusCode errorCode = HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST;
        String customMessage = "Custom error message";
        HttpParsingException exception = new HttpParsingException(errorCode, customMessage);

        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void testHttpParsingExceptionWithNullErrorCodeAndCustomMessage() {
        String customMessage = "Custom error message";
        HttpParsingException exception = new HttpParsingException(null, customMessage);

        assertNull(exception.getErrorCode());
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void testHttpParsingExceptionInheritanceMethods() {
        HttpStatusCode errorCode = HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST;
        String customMessage = "Custom error message";
        Throwable cause = new RuntimeException("Cause");
        HttpParsingException exception = new HttpParsingException(errorCode, customMessage);
        exception.initCause(cause);

        assertEquals(cause, exception.getCause());
        assertEquals("Cause", exception.getCause().getMessage());
    }
}
