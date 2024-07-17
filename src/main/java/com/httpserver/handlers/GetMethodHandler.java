package com.httpserver.handlers;

import com.http.HttpRequest;
import com.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Handler for HTTP GET requests.
 * This handler reads the requested file and sends its contents in the HTTP response.
 */
public class GetMethodHandler implements MethodHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetMethodHandler.class);
    private final File file;

    /**
     * Constructs a new GetMethodHandler.
     *
     * @param file the file to be served by the handler
     */
    public GetMethodHandler(File file) {
        this.file = file;
    }

    /**
     * Handles the HTTP GET request and prepares the HTTP response.
     * The response includes the contents of the requested file along with appropriate headers.
     *
     * @param request  the HTTP request to handle
     * @param response the HTTP response to prepare
     */
    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileContent = fis.readAllBytes();
            String mimeType = Files.probeContentType(Paths.get(file.getPath()));
            response.setStatusCode(200);
            response.setReasonPhrase("OK");
            response.setHeaders(Map.of(
                    "Content-Type", mimeType,
                    "Content-Length", String.valueOf(file.length())
            ));
            response.setBody(new String(fileContent));
        } catch (IOException e) {
            LOGGER.error("Error reading file", e);
            response.setStatusCode(500);
            response.setReasonPhrase("Internal Server Error");
            response.setBody("<html><body><h1>500 Internal Server Error</h1></body></html>");
        }
    }
}
