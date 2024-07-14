package com.http;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
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

    @Test
    void testSetHeadersCaseInsensitiveHandling() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        // Use LinkedHashMap to ensure predictable order in the test
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Host", "localhost");
        headers.put("Content-Type", "application/json");
        headers.put("HOST", "example.com"); // Different casing should overwrite

        request.setHeaders(headers);

        assertEquals(2, request.getHeaders().size());
        assertEquals("example.com", request.getHeaders().get("host"));
        assertEquals("application/json", request.getHeaders().get("content-type"));
    }

    @Test
    void testSetHeadersOverwriteBehavior() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        // Use LinkedHashMap to ensure predictable order in the test
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Accept", "text/html");
        headers.put("accept", "application/xml"); // Different casing should overwrite

        request.setHeaders(headers);

        assertEquals(1, request.getHeaders().size());
        assertEquals("application/xml", request.getHeaders().get("accept"));
    }

    @Test
    void testSetAndGetBody() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        String body = "This is the body of the request.";
        request.setBody(body);
        assertEquals(body, request.getBody());
    }

    @Test
    void testSetBodyInvalid() {
        HttpRequest request = new HttpRequest();

        assertThrows(HttpParsingException.class, () -> request.setBody(null));
    }

    @Test
    void testSetHeadersWithLargeNumberOfHeaders() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        Map<String, String> headers = new LinkedHashMap<>();
        for (int i = 0; i < 1000; i++) {
            headers.put("Header-" + i, "value-" + i);
        }

        request.setHeaders(headers);
        assertEquals(1000, request.getHeaders().size());
        for (int i = 0; i < 1000; i++) {
            assertEquals("value-" + i, request.getHeaders().get("header-" + i));
        }
    }

    @Test
    void testSetHeadersWithSpecialCharacters() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("X-Custom-Header", "value!@#$%^&*()_+{}|:\"<>?");
        headers.put("Another-Header", "another_value-=[]\\;',./`~");

        request.setHeaders(headers);
        assertEquals(2, request.getHeaders().size());
        assertEquals("value!@#$%^&*()_+{}|:\"<>?", request.getHeaders().get("x-custom-header"));
        assertEquals("another_value-=[]\\;',./`~", request.getHeaders().get("another-header"));
    }

    @Test
    void testSetHeadersEmptyMap() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        Map<String, String> headers = new LinkedHashMap<>();

        request.setHeaders(headers);
        assertEquals(0, request.getHeaders().size());
    }

    @Test
    void testSetHeadersWithMixedCaseKeys() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Header-One", "value1");
        headers.put("header-Two", "value2");
        headers.put("HEADER-THREE", "value3");

        request.setHeaders(headers);
        assertEquals(3, request.getHeaders().size());
        assertEquals("value1", request.getHeaders().get("header-one"));
        assertEquals("value2", request.getHeaders().get("header-two"));
        assertEquals("value3", request.getHeaders().get("header-three"));
    }

    @Test
    void testSetHeaderWithNullValue() {
        HttpRequest request = new HttpRequest();

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Header-With-Null-Value", null);

        assertThrows(HttpParsingException.class, () -> request.setHeaders(headers));
    }
}
