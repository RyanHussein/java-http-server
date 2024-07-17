package com.httpserver.handlers;

import com.http.HttpRequest;
import com.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Handler for HTTP HEAD requests.
 * This handler responds with the same headers as a GET request, but without the response body.
 */
public class HeadMethodHandler implements MethodHandler {
    private final File file;

    /**
     * Constructs a new HeadMethodHandler.
     *
     * @param file the file to be served by the handler
     */
    public HeadMethodHandler(File file) {
        this.file = file;
    }

    /**
     * Handles the HTTP HEAD request and prepares the HTTP response.
     * The response includes the headers as if a GET request was made, but without the response body.
     *
     * @param request  the HTTP request to handle
     * @param response the HTTP response to prepare
     * @throws IOException if an I/O error occurs during handling
     */
    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        String mimeType = Files.probeContentType(Paths.get(file.getPath()));
        response.setStatusCode(200);
        response.setReasonPhrase("OK");
        response.setHeaders(Map.of(
                "Content-Type", mimeType,
                "Content-Length", String.valueOf(file.length())
        ));
        response.setBody(""); // No body for HEAD request
    }
}
