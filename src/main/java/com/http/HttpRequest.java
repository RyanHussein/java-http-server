package com.http;

/**
 * Represents a HTTP request.
 */
public class HttpRequest extends HttpMessage {

    private HttpMethod method;
    private String requestTarget;
    private HttpVersion httpVersion;

    /**
     * Constructs an HttpRequest.
     */
    HttpRequest() {
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
     * @throws HttpParsingException if the method is not supported
     */
    void setMethod(String method) throws HttpParsingException {
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
}
