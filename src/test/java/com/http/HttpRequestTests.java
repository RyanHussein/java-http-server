package com.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTests {

    @Test
    void testSetAndGetMethod() throws HttpParsingException {
        HttpRequest request = new HttpRequest();

        request.setMethod("GET");
        assertEquals(HttpMethod.GET, request.getMethod());

        request.setMethod("HEAD");
        assertEquals(HttpMethod.HEAD, request.getMethod());
    }

    @Test
    void testSetMethodInvalid() {
        HttpRequest request = new HttpRequest();

        assertThrows(HttpParsingException.class, () -> request.setMethod("POST"));
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
}
