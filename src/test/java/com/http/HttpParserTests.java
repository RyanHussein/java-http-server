package com.http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
}