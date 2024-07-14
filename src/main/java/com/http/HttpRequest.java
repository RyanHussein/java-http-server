package com.http;

import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a HTTP request.
 */
public class HttpRequest extends HttpMessage {

    private HttpMethod method;
    private String requestTarget;
    private HttpVersion httpVersion;
    private final Map<String, String> headers;
    private String body;

    /**
     * Constructs an HttpRequest.
     */
    HttpRequest() {
        headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Gets the HTTP method of the request.
     *
     * @return the HTTP method
     */
    HttpMethod getMethod() {
        return method;
    }

    /**
     * Sets the HTTP method of the request.
     *
     * @param method the HTTP method as a string
     * @throws HttpParsingException if the method is not supported or is null
     */
    void setMethod(String method) throws HttpParsingException {
        if (method == null) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
        for (HttpMethod m : HttpMethod.values()) {
            if (method.equals(m.name())) {
                this.method = HttpMethod.valueOf(method);
                return;
            }
        }
        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }

    /**
     * Gets the request target (URI) of the request.
     *
     * @return the request target
     */
    String getRequestTarget() {
        return requestTarget;
    }

    /**
     * Sets the request target (URI) of the request.
     *
     * @param requestTarget the request target
     * @throws HttpParsingException if the request target is null or empty
     */
    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.length() == 0) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        this.requestTarget = requestTarget;
    }

    /**
     * Gets the HTTP version of the request.
     *
     * @return the HTTP version
     */
    HttpVersion getHttpVersion() {
        return httpVersion;
    }

    /**
     * Sets the HTTP version of the request.
     *
     * @param httpVersion the HTTP version as a string
     * @throws HttpParsingException if the HTTP version is not supported
     */
    void setHttpVersion(String httpVersion) throws HttpParsingException {
        if (httpVersion == null) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
        this.httpVersion = HttpVersion.getBestCompatibleVersion(httpVersion);
    }

    /**
     * Gets the headers of the request.
     *
     * @return the headers
     */
    Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets the headers of the request.
     *
     * @param headers the headers to set
     * @throws HttpParsingException if the headers map or any key or value is null or empty
     */
    void setHeaders(Map<String, String> headers) throws HttpParsingException {
        if (headers == null) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

        // Clear existing headers
        this.headers.clear();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key == null || key.trim().isEmpty()) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
            }

            if (value == null) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
            }

            // Normalise key to lower case, trim key and value, and store them
            String normalizedKey = key.trim().toLowerCase();
            String trimmedValue = value.trim();

            this.headers.put(normalizedKey, trimmedValue);
        }
    }

    /**
     * Gets the body of the request.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body of the request.
     *
     * @param body the body to set
     * @throws HttpParsingException if the body is null
     */
    public void setBody(String body) throws HttpParsingException {
        if (body == null) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        this.body = body;
    }
}
