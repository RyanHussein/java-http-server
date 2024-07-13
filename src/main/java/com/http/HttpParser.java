package com.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Parses HTTP requests from an input stream.
 */
public class HttpParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; // Space
    private static final int CR = 0x0D; // Carriage Return
    private static final int LF = 0x0A; // Line Feed
    private static final int MAX_REQUEST_LINE_LENGTH = 8192;

    /**
     * Parses an HTTP request from the given input stream.
     *
     * @param inputStream the input stream to read from
     * @return the parsed HttpRequest object
     * @throws HttpParsingException if there is an error parsing the request
     * @throws IOException if there is an I/O error
     */
    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException, IOException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();

        parseRequestLine(reader, request);
        parseHeaders(reader, request);
        parseBody(reader, request);

        return request;
    }

    /**
     * Parses the request line from the input stream reader and updates the HttpRequest object.
     *
     * @param reader the InputStreamReader to read from
     * @param request the HttpRequest object to update
     * @throws HttpParsingException if there is an error parsing the request line
     * @throws IOException if there is an I/O error
     */
    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws HttpParsingException, IOException {
        StringBuilder processingDataBuffer = new StringBuilder();

        int _byte;
        int stage = 0; // 0: method, 1: URI, 2: HTTP version

        while ((_byte = reader.read()) >= 0) {
            if (processingDataBuffer.length() > MAX_REQUEST_LINE_LENGTH) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_414_URI_TOO_LONG);
            }

            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    if (stage != 2) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST); // Incomplete request line
                    }
                    request.setHttpVersion(processingDataBuffer.toString());
                    return;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST); // Invalid line ending
                }
            } else if (_byte == SP) {
                if (stage == 0) {
                    if (processingDataBuffer.length() == 0) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST); // Missing method
                    }
                    request.setMethod(processingDataBuffer.toString());
                    stage = 1;
                } else if (stage == 1) {
                    if (processingDataBuffer.length() == 0) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST); // Missing URI
                    }
                    request.setRequestTarget(processingDataBuffer.toString());
                    stage = 2;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST); // Extra space in request line
                }
                processingDataBuffer.setLength(0); // Clear the buffer
            } else {
                processingDataBuffer.append((char) _byte);
            }
        }

        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST); // Premature end of input
    }

    /**
     * Parses the headers from the input stream reader and updates the HttpRequest object.
     *
     * @param reader the InputStreamReader to read from
     * @param request the HttpRequest object to update
     */
    private void parseHeaders(InputStreamReader reader, HttpRequest request) {
        // Implementation for parsing headers
    }

    /**
     * Parses the body from the input stream reader and updates the HttpRequest object.
     *
     * @param reader the InputStreamReader to read from
     * @param request the HttpRequest object to update
     */
    private void parseBody(InputStreamReader reader, HttpRequest request) {
        // Implementation for parsing body
    }
}
