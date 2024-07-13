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
}