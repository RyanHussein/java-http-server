package com.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a HTTP response.
 */
public class HttpResponse {

    private int statusCode;
    private String reasonPhrase;
    private final Map<String, String> headers;
    private String body;
    private final HttpVersion httpVersion;

    /**
     * Constructs an HttpResponse with the specified HTTP version.
     *
     * @param httpVersion the HTTP version to use for the response
     */
    public HttpResponse(HttpVersion httpVersion) {
        this.headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        this.httpVersion = httpVersion;
    }

    /**
     * Gets the status code of the response.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the status code of the response.
     *
     * @param statusCode the status code to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets the reason phrase of the response.
     *
     * @return the reason phrase
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * Sets the reason phrase of the response.
     *
     * @param reasonPhrase the reason phrase to set
     */
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * Gets the headers of the response.
     *
     * @return the headers map
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets multiple headers for the response.
     *
     * @param headers the headers to set
     */
    public void setHeaders(Map<String, String> headers) {
        if (headers != null) {
            headers.forEach((key, value) -> {
                if (key != null && value != null) {
                    this.headers.put(key, value);
                }
            });
        }
    }

    /**
     * Sets a single header for the response.
     *
     * @param name  the name of the header
     * @param value the value of the header
     */
    public void setHeader(String name, String value) {
        if (name != null && value != null) {
            this.headers.put(name, value);
        }
    }

    /**
     * Gets the body of the response.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body of the response and updates the Content-Length header.
     *
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Writes the HTTP response to the specified output stream.
     *
     * @param outputStream the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    public void write(OutputStream outputStream) throws IOException {
        StringBuilder response = new StringBuilder();

        // Status line
        response.append(httpVersion.getLiteral()).append(" ").append(statusCode).append(" ").append(reasonPhrase).append("\r\n");
        // Headers
        headers.forEach((name, value) -> response.append(name).append(": ").append(value).append("\r\n"));
        // Blank line between headers and body
        response.append("\r\n");

        // Write headers to the output stream
        outputStream.write(response.toString().getBytes(StandardCharsets.UTF_8));

        // Write body to the output stream, if it exists
        if (body != null && !body.isEmpty()) {
            outputStream.write(body.getBytes(StandardCharsets.UTF_8));
        }
    }
}
