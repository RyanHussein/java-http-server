package com.http;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HttpStatusCodeTests {

    @Test
    void testHttpStatusCodeValues() {
        // Ensure the enum has the correct values
        assertEquals(400, HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST.STATUS_CODE);
        assertEquals("Bad Request", HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST.MESSAGE);

        assertEquals(405, HttpStatusCode.CLIENT_ERROR_405_METHOD_NOT_ALLOWED.STATUS_CODE);
        assertEquals("Method Not Allowed", HttpStatusCode.CLIENT_ERROR_405_METHOD_NOT_ALLOWED.MESSAGE);

        assertEquals(414, HttpStatusCode.CLIENT_ERROR_414_URI_TOO_LONG.STATUS_CODE);
        assertEquals("URI Too Long", HttpStatusCode.CLIENT_ERROR_414_URI_TOO_LONG.MESSAGE);

        assertEquals(500, HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR.STATUS_CODE);
        assertEquals("Internal Server Error", HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR.MESSAGE);

        assertEquals(501, HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED.STATUS_CODE);
        assertEquals("Not Implemented", HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED.MESSAGE);

        assertEquals(505, HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED.STATUS_CODE);
        assertEquals("HTTP Version Not Supported", HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED.MESSAGE);
    }

    @Test
    void testHttpStatusCodeEnumOrder() {
        // Ensure the enum values are in the correct order
        HttpStatusCode[] statusCodes = HttpStatusCode.values();
        assertEquals(6, statusCodes.length);

        assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, statusCodes[0]);
        assertEquals(HttpStatusCode.CLIENT_ERROR_405_METHOD_NOT_ALLOWED, statusCodes[1]);
        assertEquals(HttpStatusCode.CLIENT_ERROR_414_URI_TOO_LONG, statusCodes[2]);
        assertEquals(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR, statusCodes[3]);
        assertEquals(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, statusCodes[4]);
        assertEquals(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED, statusCodes[5]);
    }

    @Test
    void testHttpStatusCodeFromInt() {
        // Ensure you can retrieve the enum constant by status code
        assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, HttpStatusCode.fromCode(400));
        assertEquals(HttpStatusCode.CLIENT_ERROR_405_METHOD_NOT_ALLOWED, HttpStatusCode.fromCode(405));
        assertEquals(HttpStatusCode.CLIENT_ERROR_414_URI_TOO_LONG, HttpStatusCode.fromCode(414));
        assertEquals(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR, HttpStatusCode.fromCode(500));
        assertEquals(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, HttpStatusCode.fromCode(501));
        assertEquals(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED, HttpStatusCode.fromCode(505));
    }

    @Test
    void testInvalidHttpStatusCode() {
        // Ensure invalid status codes return null or throw an appropriate exception
        assertNull(HttpStatusCode.fromCode(999));
        assertNull(HttpStatusCode.fromCode(-1));
    }
}
