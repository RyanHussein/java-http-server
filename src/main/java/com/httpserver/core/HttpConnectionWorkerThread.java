package com.httpserver.core;

import com.http.HttpMethod;
import com.http.HttpParser;
import com.http.HttpParsingException;
import com.http.HttpRequest;
import com.http.HttpResponse;
import com.http.HttpVersion;
import com.httpserver.handlers.GetMethodHandler;
import com.httpserver.handlers.HeadMethodHandler;
import com.httpserver.handlers.MethodHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.EnumMap;
import java.util.Map;

/**
 * A thread that handles an individual HTTP connection.
 */
public class HttpConnectionWorkerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    private final Socket socket;
    private final String webroot;
    private final Map<HttpMethod, MethodHandler> methodHandlers;

    /**
     * Constructs a new HttpConnectionWorkerThread.
     *
     * @param socket the socket connected to the client
     * @param webroot the root directory for web content
     */
    public HttpConnectionWorkerThread(Socket socket, String webroot) {
        this.socket = socket;
        this.webroot = webroot;
        this.methodHandlers = new EnumMap<>(HttpMethod.class);
    }

    /**
     * Runs the thread to process the HTTP connection.
     */
    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {
            HttpResponse response;

            // Parse the request
            HttpParser parser = new HttpParser();
            HttpRequest request;
            try {
                request = parser.parseHttpRequest(inputStream);
                response = new HttpResponse(request.getHttpVersion());
            } catch (HttpParsingException e) {
                LOGGER.error("Error parsing HTTP request", e);
                response = new HttpResponse(HttpVersion.HTTP_1_1); // Default to HTTP/1.1 if parsing fails
                sendErrorResponse(response, 400, "Bad Request", outputStream);
                return;
            }

            // Basic routing
            handleRequest(request, response);

            // Send response
            response.write(outputStream);

            LOGGER.info("Connection processing finished.");
        } catch (IOException e) {
            LOGGER.error("Error with communication: ", e);
        } finally {
            closeSocket();
        }
    }

    /**
     * Handles the incoming HTTP request and prepares the appropriate response.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     */
    private void handleRequest(HttpRequest request, HttpResponse response) {
        String requestTarget = request.getRequestTarget();
        File file = new File(webroot, requestTarget);

        if (file.exists() && !file.isDirectory()) {
            populateMethodHandlers(file);

            MethodHandler handler = methodHandlers.get(request.getMethod());

            if (handler != null) {
                try {
                    handler.handle(request, response);
                } catch (IOException e) {
                    LOGGER.error("Error handling request", e);
                    sendErrorResponse(response, 500, "Internal Server Error", null);
                }
            } else {
                sendErrorResponse(response, 405, "Method Not Allowed", null);
                response.setHeader("Allow", "GET, HEAD");
            }
        } else {
            sendErrorResponse(response, 404, "Not Found", null);
        }
    }

    /**
     * Populates the method handlers for GET and HEAD requests.
     *
     * @param file the file requested by the client
     */
    private void populateMethodHandlers(File file) {
        methodHandlers.put(HttpMethod.GET, new GetMethodHandler(file));
        methodHandlers.put(HttpMethod.HEAD, new HeadMethodHandler(file));
    }

    /**
     * Sends an error response to the client.
     *
     * @param response the HTTP response
     * @param statusCode the HTTP status code
     * @param reasonPhrase the reason phrase
     * @param outputStream the output stream to write the response to
     */
    private void sendErrorResponse(HttpResponse response, int statusCode, String reasonPhrase, OutputStream outputStream) {
        response.setStatusCode(statusCode);
        response.setReasonPhrase(reasonPhrase);
        response.setHeader("Content-Type", "text/html");
        response.setBody(String.format("<html><body><h1>%d %s</h1></body></html>", statusCode, reasonPhrase));
        if (outputStream != null) {
            try {
                response.write(outputStream);
            } catch (IOException e) {
                LOGGER.error("Error sending error response", e);
            }
        }
    }

    /**
     * Closes the socket if it is open.
     */
    private void closeSocket() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Error closing socket: ", e);
            }
        }
    }
}
