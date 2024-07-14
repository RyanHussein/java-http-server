package com.http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpParserTests {

    private final HttpParser parser = new HttpParser();

    @Test
    void testParseRequestLineValid() throws Exception {
        String rawRequest = "GET / HTTP/1.1\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
        HttpRequest request = parser.parseHttpRequest(inputStream);

        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/", request.getRequestTarget());
        assertEquals(HttpVersion.HTTP_1_1, request.getHttpVersion());
    }

    @Test
    void testParseRequestLineInvalidMethod() {
        String rawRequest = "INVALID / HTTP/1.1\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));

        assertThrows(HttpParsingException.class, () -> parser.parseHttpRequest(inputStream));
    }

    @Test
    void testParseRequestLineMissingURI() {
        String rawRequest = "GET  HTTP/1.1\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));

        assertThrows(HttpParsingException.class, () -> parser.parseHttpRequest(inputStream));
    }

    @Test
    void testParseRequestLineInvalidHTTPVersion() {
        String rawRequest = "GET / INVALID\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));

        assertThrows(HttpParsingException.class, () -> parser.parseHttpRequest(inputStream));
    }

    @Test
    void testParseRequestLineTooLong() {
        String rawRequest = "GET " + "a".repeat(8200) + " HTTP/1.1\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));

        assertThrows(HttpParsingException.class, () -> parser.parseHttpRequest(inputStream));
    }

    @Test
    void testParseRequestLinePrematureEnd() {
        String rawRequest = "GET / HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));

        assertThrows(HttpParsingException.class, () -> parser.parseHttpRequest(inputStream));
    }

    @Test
    void testParseHeadersValid() throws Exception {
        String rawRequest = "GET / HTTP/1.1\r\nHost: localhost\r\nConnection: keep-alive\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
        HttpRequest request = parser.parseHttpRequest(inputStream);

        Map<String, String> headers = request.getHeaders();
        assertEquals(2, headers.size());
        assertEquals("localhost", headers.get("Host"));
        assertEquals("keep-alive", headers.get("Connection"));
    }

    @Test
    void testParseHeadersInvalid() {
        String rawRequest = "GET / HTTP/1.1\r\nInvalid-Header\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));

        assertThrows(HttpParsingException.class, () -> parser.parseHttpRequest(inputStream));
    }

    @Test
    void testParseHeadersContinuation() throws Exception {
        String rawRequest = "GET / HTTP/1.1\r\nHost: localhost\r\n Connection: keep-alive\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
        HttpRequest request = parser.parseHttpRequest(inputStream);

        Map<String, String> headers = request.getHeaders();
        assertEquals(1, headers.size());
        assertEquals("localhost Connection: keep-alive", headers.get("Host"));
    }

    @Test
    void testParseHeadersEmptyValue() throws Exception {
        String rawRequest = "GET / HTTP/1.1\r\nHost: \r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
        HttpRequest request = parser.parseHttpRequest(inputStream);

        Map<String, String> headers = request.getHeaders();
        assertEquals(1, headers.size());
        assertEquals("", headers.get("Host"));
    }

    @Test
    void testParseHeadersEndOfHeaders() throws Exception {
        String rawRequest = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
        HttpRequest request = parser.parseHttpRequest(inputStream);

        Map<String, String> headers = request.getHeaders();
        assertEquals(1, headers.size());
        assertEquals("localhost", headers.get("Host"));
    }

    @Test
    void testParseHeadersCaseInsensitive() throws Exception {
        String rawRequest = "GET / HTTP/1.1\r\nhOsT: localhost\r\ncOnNeCtIoN: keep-alive\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
        HttpRequest request = parser.parseHttpRequest(inputStream);

        Map<String, String> headers = request.getHeaders();
        assertEquals(2, headers.size());
        assertEquals("localhost", headers.get("host"));
        assertEquals("keep-alive", headers.get("connection"));
    }

    @Test
    void testParseHeadersMalformedMissingColon() {
        String rawRequest = "GET / HTTP/1.1\r\nInvalidHeader\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));

        assertThrows(HttpParsingException.class, () -> parser.parseHttpRequest(inputStream));
    }

    @Test
    void testParseHeadersLeadingTrailingWhitespace() throws Exception {
        String rawRequest = "GET / HTTP/1.1\r\nHost:  localhost  \r\nConnection:  keep-alive  \r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
        HttpRequest request = parser.parseHttpRequest(inputStream);

        Map<String, String> headers = request.getHeaders();
        assertEquals(2, headers.size());
        assertEquals("localhost", headers.get("Host"));
        assertEquals("keep-alive", headers.get("Connection"));
    }

    @Test
    void testParseHeadersDuplicate() throws Exception {
        String rawRequest = "GET / HTTP/1.1\r\nHost: localhost\r\nHost: example.com\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
        HttpRequest request = parser.parseHttpRequest(inputStream);

        Map<String, String> headers = request.getHeaders();
        assertEquals(1, headers.size());
        assertEquals("example.com", headers.get("Host")); // Assuming the last header value is used
    }

}
