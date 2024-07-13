package com.http;

/**
 * Enum representing HTTP status codes and their associated messages.
 */
public enum HttpStatusCode {

    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_405_METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    CLIENT_ERROR_414_URI_TOO_LONG(414, "URI Too Long"),

    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501, "Not Implemented"),
    SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");

    public final int STATUS_CODE;
    public final String MESSAGE;

    /**
     * Constructs an HttpStatusCode enum constant.
     *
     * @param STATUS_CODE the numerical HTTP status code
     * @param MESSAGE the associated message for the status code
     */
    HttpStatusCode(int STATUS_CODE, String MESSAGE) {
        this.STATUS_CODE = STATUS_CODE;
        this.MESSAGE = MESSAGE;
    }

    /**
     * Returns the HttpStatusCode enum constant corresponding to the given status code.
     *
     * @param code the numerical HTTP status code
     * @return the HttpStatusCode enum constant, or null if no constant is found
     */
     static HttpStatusCode fromCode(int code) {
        for (HttpStatusCode status : HttpStatusCode.values()) {
            if (status.STATUS_CODE == code) {
                return status;
            }
        }
        return null;
    }
}
