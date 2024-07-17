package com.http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HttpResponse class.
 */
class HttpResponseTests {

    @Test
    void testSetAndGetStatusCode() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        response.setStatusCode(200);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void testSetAndGetReasonPhrase() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        response.setReasonPhrase("OK");
        assertEquals("OK", response.getReasonPhrase());
    }

    @Test
    void testSetAndGetHeaders() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);

        Map<String, String> headers = Map.of(
                "Content-Type", "text/html",
                "Content-Length", "100"
        );

        response.setHeaders(headers);
        assertEquals(headers, response.getHeaders());
    }

    @Test
    void testSetHeader() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        response.setHeader("Content-Type", "text/html");
        assertEquals("text/html", response.getHeaders().get("Content-Type"));
    }

    @Test
    void testSetAndGetBody() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        String body = "<html><body>Hello, World!</body></html>";
        response.setBody(body);
        assertEquals(body, response.getBody());
    }

    @Test
    void testWriteResponseWithoutBody() throws IOException {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        response.setStatusCode(204);
        response.setReasonPhrase("No Content");
        response.setHeader("Content-Type", "text/html");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        response.write(outputStream);

        String expectedResponse = "HTTP/1.1 204 No Content\r\nContent-Type: text/html\r\n\r\n";
        assertEquals(expectedResponse, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void testWriteResponseWithNullBody() throws IOException {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        response.setStatusCode(204);
        response.setReasonPhrase("No Content");
        response.setHeader("Content-Type", "text/html");
        response.setBody(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        response.write(outputStream);

        String expectedResponse = "HTTP/1.1 204 No Content\r\nContent-Type: text/html\r\n\r\n";
        assertEquals(expectedResponse, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void testSetHeadersWithNullValue() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Content-Length", "123");
        headers.put("X-Null-Header", null);

        response.setHeaders(headers);

        assertEquals("text/html", response.getHeaders().get("Content-Type"));
        assertEquals("123", response.getHeaders().get("Content-Length"));
        assertNull(response.getHeaders().get("X-Null-Header"));
    }

    @Test
    void testSetHeaderWithNullValue() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        response.setHeader("Content-Length", null);
        assertNull(response.getHeaders().get("Content-Length"));
    }

    @Test
    void testInitialHeadersAreEmpty() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        assertTrue(response.getHeaders().isEmpty());
    }

    @Test
    void testInitialBodyIsNull() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        assertNull(response.getBody());
    }

    @Test
    void testInitialStatusCodeIsZero() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        assertEquals(0, response.getStatusCode());
    }

    @Test
    void testInitialReasonPhraseIsNull() {
        HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1);
        assertNull(response.getReasonPhrase());
    }
}
