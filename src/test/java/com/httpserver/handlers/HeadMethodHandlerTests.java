package com.httpserver.handlers;

import com.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HeadMethodHandler class.
 */
class HeadMethodHandlerTests {

    private HeadMethodHandler headMethodHandler;
    private HttpRequest request;
    private HttpResponse response;
    private static final String WEBROOT = "./webroot";

    @BeforeEach
    void setUp() throws HttpParsingException {
        request = new HttpRequest();
        request.setMethod(HttpMethod.HEAD.name());
        request.setHttpVersion("HTTP/1.1");
        response = new HttpResponse(HttpVersion.HTTP_1_1);
    }

    @Test
    void testHandleRequestWithIndexHtml() throws IOException, HttpParsingException {
        request.setRequestTarget("/index.html");
        File file = new File(WEBROOT, request.getRequestTarget());
        assertTrue(file.exists(), "File should exist: " + file.getAbsolutePath());
        assertTrue(file.length() > 0, "File length should be greater than 0: " + file.getAbsolutePath());
        headMethodHandler = new HeadMethodHandler(file);

        headMethodHandler.handle(request, response);

        System.out.println("After handle method: " + response.getHeaders().get("Content-Length"));

        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getReasonPhrase());
        assertEquals("", response.getBody());
        assertEquals(Files.probeContentType(Paths.get(file.getPath())), response.getHeaders().get("Content-Type"));
        assertEquals(String.valueOf(file.length()), response.getHeaders().get("Content-Length"));
    }

    @Test
    void testHandleRequestWithPage1Html() throws IOException, HttpParsingException {
        request.setRequestTarget("/page1.html");
        File file = new File(WEBROOT, request.getRequestTarget());
        assertTrue(file.exists(), "File should exist: " + file.getAbsolutePath());
        assertTrue(file.length() > 0, "File length should be greater than 0: " + file.getAbsolutePath());
        headMethodHandler = new HeadMethodHandler(file);

        headMethodHandler.handle(request, response);

        System.out.println("After handle method: " + response.getHeaders().get("Content-Length"));

        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getReasonPhrase());
        assertEquals("", response.getBody());
        assertEquals(Files.probeContentType(Paths.get(file.getPath())), response.getHeaders().get("Content-Type"));
        assertEquals(String.valueOf(file.length()), response.getHeaders().get("Content-Length"));
    }

    @Test
    void testHandleRequestWithNonExistentFile() throws HttpParsingException {
        request.setRequestTarget("/nonexistent.html");
        File file = new File(WEBROOT, request.getRequestTarget());
        headMethodHandler = new HeadMethodHandler(file);

        try {
            headMethodHandler.handle(request, response);
        } catch (IOException e) {
            assertEquals("No such file or directory", e.getMessage());
        }
    }
}
