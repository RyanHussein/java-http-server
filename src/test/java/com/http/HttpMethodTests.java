package com.http;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HttpMethod enum class.
 */
class HttpMethodTests {

    @Test
    void testHttpMethodValues() {
        // Ensure the enum has the correct values
        assertEquals("GET", HttpMethod.GET.name());
        assertEquals("HEAD", HttpMethod.HEAD.name());

        // Ensure the enum values are in the correct order
        HttpMethod[] methods = HttpMethod.values();
        assertEquals(2, methods.length);
        assertEquals(HttpMethod.GET, methods[0]);
        assertEquals(HttpMethod.HEAD, methods[1]);
    }

    @Test
    void testHttpMethodValueOf() {
        // Ensure valueOf works for valid enum names
        assertEquals(HttpMethod.GET, HttpMethod.valueOf("GET"));
        assertEquals(HttpMethod.HEAD, HttpMethod.valueOf("HEAD"));
    }

    @Test
    void testInvalidHttpMethod() {
        // Ensure valueOf throws IllegalArgumentException for invalid names
        assertThrows(IllegalArgumentException.class, () -> HttpMethod.valueOf("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> HttpMethod.valueOf(""));
    }

    @Test
    void testNullHttpMethod() {
        // Ensure valueOf throws NullPointerException for null
        assertThrows(NullPointerException.class, () -> HttpMethod.valueOf(null));
    }

    @Test
    void testHttpMethodToString() {
        // Ensure toString returns the correct string representation
        assertEquals("GET", HttpMethod.GET.toString());
        assertEquals("HEAD", HttpMethod.HEAD.toString());
    }
}
