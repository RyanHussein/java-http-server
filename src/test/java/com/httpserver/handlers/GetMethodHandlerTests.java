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
 * Unit tests for the GetMethodHandler class.
 */
class GetMethodHandlerTests {

    private GetMethodHandler getMethodHandler;
    private HttpRequest request;
    private HttpResponse response;
    private static final String WEBROOT = "./webroot";

    @BeforeEach
    void setUp() throws HttpParsingException {
        request = new HttpRequest();
        request.setMethod(HttpMethod.GET.name());
        request.setHttpVersion("HTTP/1.1");
        response = new HttpResponse(HttpVersion.HTTP_1_1);
    }

    @Test
    void testHandleRequestWithIndexHtml() throws IOException, HttpParsingException {
        request.setRequestTarget("/index.html");
        File file = new File(WEBROOT, request.getRequestTarget());
        getMethodHandler = new GetMethodHandler(file);

        getMethodHandler.handle(request, response);

        String expectedBody = new String(Files.readAllBytes(file.toPath()));

        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getReasonPhrase());
        assertEquals(expectedBody, response.getBody());
        assertEquals(Files.probeContentType(Paths.get(file.getPath())), response.getHeaders().get("Content-Type"));
        assertEquals(String.valueOf(file.length()), response.getHeaders().get("Content-Length"));
    }

    @Test
    void testHandleRequestWithPage1Html() throws IOException, HttpParsingException {
        request.setRequestTarget("/page1.html");
        File file = new File(WEBROOT, request.getRequestTarget());
        getMethodHandler = new GetMethodHandler(file);

        getMethodHandler.handle(request, response);

        String expectedBody = new String(Files.readAllBytes(file.toPath()));

        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getReasonPhrase());
        assertEquals(expectedBody, response.getBody());
        assertEquals(Files.probeContentType(Paths.get(file.getPath())), response.getHeaders().get("Content-Type"));
        assertEquals(String.valueOf(file.length()), response.getHeaders().get("Content-Length"));
    }

    @Test
    void testHandleRequestWithNonExistentFile() throws HttpParsingException {
        request.setRequestTarget("/nonexistent.html");
        File file = new File(WEBROOT, request.getRequestTarget());
        getMethodHandler = new GetMethodHandler(file);

        getMethodHandler.handle(request, response);

        assertEquals(500, response.getStatusCode());
        assertEquals("Internal Server Error", response.getReasonPhrase());
        assertEquals("<html><body><h1>500 Internal Server Error</h1></body></html>", response.getBody());
    }
}
