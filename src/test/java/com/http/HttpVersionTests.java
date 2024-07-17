package com.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HttpVersion enum class.
 */
class HttpVersionTests {

    @Test
    void testGetBestCompatibleVersionExactMatch() throws HttpParsingException {
        HttpVersion version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        assertEquals(HttpVersion.HTTP_1_1, version);
    }

    @Test
    void testGetBestCompatibleVersionIncompatibleVersion() {
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion("HTTP/2.0"));
    }

    @Test
    void testGetBestCompatibleVersionInvalidFormat() {
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion("HTTP/1"));
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion("HTP/1.1"));
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion("HTTP/one.one"));
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion("1.1"));
    }

    @Test
    void testGetBestCompatibleVersionNullInput() {
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion(null));
    }

    @Test
    void testGetBestCompatibleVersionEmptyInput() {
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion(""));
    }

    @Test
    void testGetBestCompatibleVersionOnlyHTTP() {
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion("HTTP/"));
    }

    @Test
    void testGetBestCompatibleVersionWhitespace() {
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion("HTTP/1. 1"));
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion(" HTTP/1.1"));
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion("HTTP/1.1 "));
    }

    @Test
    void testGetBestCompatibleVersionWithLowercase() {
        assertThrows(HttpParsingException.class, () -> HttpVersion.getBestCompatibleVersion("http/1.1"));
    }

    @Test
    void testGetLiteral() {
        assertEquals("HTTP/1.1", HttpVersion.HTTP_1_1.getLiteral());
    }
}
