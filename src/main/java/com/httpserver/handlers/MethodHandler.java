package com.httpserver.handlers;

import com.http.HttpRequest;
import com.http.HttpResponse;

import java.io.IOException;

/**
 * Interface representing a handler for an HTTP method.
 * Implementations of this interface will handle specific HTTP methods
 * such as GET, POST, HEAD, etc.
 */
public interface MethodHandler {

    /**
     * Handles the HTTP request and prepares the HTTP response.
     *
     * @param request the HTTP request to handle
     * @param response the HTTP response to prepare
     * @throws IOException if an I/O error occurs during handling
     */
    void handle(HttpRequest request, HttpResponse response) throws IOException;
}