package com.http;

/**
 * Exception thrown when there is an error parsing an HTTP request.
 */
public class HttpParsingException extends Exception {

    private final HttpStatusCode errorCode;

    /**
     * Constructs a new HttpParsingException with the specified error code.
     *
     * @param errorCode the HTTP status code representing the error
     */
    public HttpParsingException(HttpStatusCode errorCode) {
        super(errorCode != null ? errorCode.MESSAGE : null);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new HttpParsingException with the specified error code and detailed message.
     *
     * @param errorCode the HTTP status code representing the error
     * @param message the detailed message
     */
    public HttpParsingException(HttpStatusCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Gets the HTTP status code representing the error.
     *
     * @return the HTTP status code
     */
    public HttpStatusCode getErrorCode() {
        return errorCode;
    }
}
