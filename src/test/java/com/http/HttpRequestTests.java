package com.http;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTests {

    @Test
    void testSetAndGetMethod() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        for (HttpMethod method : HttpMethod.values()) {
            request.setMethod(method.name());
            assertEquals(method, request.getMethod());
        }
    }

    @Test
    void testSetMethodInvalid() {
        HttpRequest request = new HttpRequest();

        assertThrows(HttpParsingException.class, () -> request.setMethod("INVALID"));
        assertThrows(HttpParsingException.class, () -> request.setMethod(""));
        assertThrows(HttpParsingException.class, () -> request.setMethod(null));
    }

    @Test
    void testSetAndGetRequestTarget() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        request.setRequestTarget("/index.html");
        assertEquals("/index.html", request.getRequestTarget());

        request.setRequestTarget("/about");
        assertEquals("/about", request.getRequestTarget());

        // Test long request target
        String longRequestTarget = "/" + "a".repeat(8192);
        request.setRequestTarget(longRequestTarget);
        assertEquals(longRequestTarget, request.getRequestTarget());
    }

    @Test
    void testSetRequestTargetInvalid() {
        HttpRequest request = new HttpRequest();

        assertThrows(HttpParsingException.class, () -> request.setRequestTarget(null));
        assertThrows(HttpParsingException.class, () -> request.setRequestTarget(""));
    }

    @Test
    void testSetAndGetHttpVersion() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        request.setHttpVersion("HTTP/1.1");
        assertEquals(HttpVersion.HTTP_1_1, request.getHttpVersion());
    }

    @Test
    void testSetHttpVersionInvalid() {
        HttpRequest request = new HttpRequest();

        assertThrows(HttpParsingException.class, () -> request.setHttpVersion(null));
        assertThrows(HttpParsingException.class, () -> request.setHttpVersion("HTTP/2.0"));
        assertThrows(HttpParsingException.class, () -> request.setHttpVersion(""));
    }

    @Test
    void testSetAndGetHeaders() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost");
        headers.put("Connection", "keep-alive");

        request.setHeaders(headers);
        assertEquals(headers, request.getHeaders());

        // Test with long header value
        headers.put("Long-Header", "a".repeat(8192));
        request.setHeaders(headers);
        assertEquals(headers, request.getHeaders());
    }

    @Test
    void testSetHeadersInvalid() {
        HttpRequest request = new HttpRequest();

        assertThrows(HttpParsingException.class, () -> request.setHeaders(null));

        Map<String, String> headersWithNullKey = new HashMap<>();
        headersWithNullKey.put(null, "value");
        assertThrows(HttpParsingException.class, () -> request.setHeaders(headersWithNullKey));

        Map<String, String> headersWithEmptyKey = new HashMap<>();
        headersWithEmptyKey.put("", "value");
        assertThrows(HttpParsingException.class, () -> request.setHeaders(headersWithEmptyKey));

        Map<String, String> headersWithNullValue = new HashMap<>();
        headersWithNullValue.put("key", null);
        assertThrows(HttpParsingException.class, () -> request.setHeaders(headersWithNullValue));
    }

    @Test
    void testSetHeadersWhitespace() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        Map<String, String> headers = new HashMap<>();
        headers.put(" Host ", " localhost ");
        headers.put("Connection", " keep-alive ");

        request.setHeaders(headers);

        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Host", "localhost");
        expectedHeaders.put("Connection", "keep-alive");

        assertEquals(expectedHeaders, request.getHeaders());
    }

    @Test
    void testSetDuplicateHeaders() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost");
        headers.put("Host", "example.com"); // Duplicate key with different value

        request.setHeaders(headers);
        assertEquals(1, request.getHeaders().size());
        assertEquals("example.com", request.getHeaders().get("Host"));
    }
}
