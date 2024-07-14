package com.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses HTTP requests from an input stream.
 */
public class HttpParser {

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

        // Read the request line
        while ((_byte = reader.read()) >= 0) {
            if (processingDataBuffer.length() > MAX_REQUEST_LINE_LENGTH) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_414_URI_TOO_LONG);
            }

            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    break;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST); // Invalid line ending
                }
            } else if (_byte == SP) {
                if (stage == 0) {
                    request.setMethod(processingDataBuffer.toString());
                    stage = 1;
                } else if (stage == 1) {
                    request.setRequestTarget(processingDataBuffer.toString());
                    stage = 2;
                }
                processingDataBuffer.setLength(0); // Clear the buffer
            } else {
                processingDataBuffer.append((char) _byte);
            }
        }

        if (stage != 2 || processingDataBuffer.length() == 0) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST); // Incomplete request line
        }

        request.setHttpVersion(processingDataBuffer.toString());
    }

    /**
     * Parses the headers from the input stream reader and updates the HttpRequest object.
     *
     * @param reader the InputStreamReader to read from
     * @param request the HttpRequest object to update
     * @throws HttpParsingException if there is an error parsing the headers
     * @throws IOException if there is an I/O error
     */
    private void parseHeaders(InputStreamReader reader, HttpRequest request) throws HttpParsingException, IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        String currentHeader = null;

        while ((line = readLine(reader)) != null && !line.isEmpty()) {
            if (Character.isWhitespace(line.charAt(0)) && currentHeader != null) {
                // Continuation of the previous header line
                headers.put(currentHeader, headers.get(currentHeader) + " " + line.trim());
            } else {
                int colonIndex = line.indexOf(":");
                if (colonIndex == -1) {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST); // Invalid header line
                }
                currentHeader = line.substring(0, colonIndex).trim();
                String headerValue = line.substring(colonIndex + 1).trim();
                headers.put(currentHeader, headerValue);
            }
        }

        request.setHeaders(headers);
    }

    /**
     * Parses the body from the input stream reader and updates the HttpRequest object.
     *
     * @param reader the InputStreamReader to read from
     * @param request the HttpRequest object to update
     * @throws HttpParsingException if there is an error parsing the body
     * @throws IOException if there is an I/O error
     */
    private void parseBody(InputStreamReader reader, HttpRequest request) throws HttpParsingException, IOException {
        Map<String, String> headers = request.getHeaders();
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] body = new char[contentLength];
            int bytesRead = reader.read(body, 0, contentLength);

            if (bytesRead != contentLength) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST,
                        "Incomplete body: Expected " + contentLength + " bytes, but read " + bytesRead);
            }

            request.setBody(new String(body));
        } else if ("chunked".equalsIgnoreCase(headers.get("Transfer-Encoding"))) {
            request.setBody(parseChunkedBody(reader));
        } else {
            request.setBody(""); // No body
        }
    }

    /**
     * Parses the chunked body from the input stream reader.
     *
     * @param reader the InputStreamReader to read from
     * @return the parsed body as a string
     * @throws HttpParsingException if there is an error parsing the chunked body
     * @throws IOException if there is an I/O error
     */
    private String parseChunkedBody(InputStreamReader reader) throws IOException, HttpParsingException {
        StringBuilder body = new StringBuilder();
        String line;

        while (true) {
            // Read the chunk size line
            line = readLine(reader);
            if (line == null || line.trim().isEmpty()) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, "Invalid chunk size line");
            }

            int chunkSize;
            try {
                chunkSize = Integer.parseInt(line.trim(), 16);
            } catch (NumberFormatException e) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, "Invalid chunk size: " + line.trim());
            }

            if (chunkSize == 0) {
                // Read the trailing CRLF after the last chunk
                line = readLine(reader);
                if (line == null || !line.isEmpty()) {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, "Invalid chunk ending: expected CRLF but got: " + line);
                }
                break;
            }

            char[] chunk = new char[chunkSize];
            int bytesRead = reader.read(chunk, 0, chunkSize);
            if (bytesRead != chunkSize) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, "Incomplete chunk: Expected " + chunkSize + " bytes, but read " + bytesRead);
            }

            body.append(chunk);
            // Read the trailing CRLF after each chunk
            line = readLine(reader);
            if (line == null || !line.isEmpty()) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, "Invalid chunk ending: expected CRLF but got: " + line);
            }
        }

        return body.toString();
    }

    /**
     * Reads a line from the input stream reader.
     *
     * @param reader the InputStreamReader to read from
     * @return the line read from the input stream, or null if end of stream is reached
     * @throws IOException if there is an I/O error
     */
    private String readLine(InputStreamReader reader) throws IOException {
        StringBuilder line = new StringBuilder();
        int _byte;
        while ((_byte = reader.read()) >= 0) {
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    break;
                } else {
                    throw new IOException("Invalid line ending: expected LF after CR");
                }
            }
            line.append((char) _byte);
        }
        return line.length() == 0 && _byte < 0 ? null : line.toString();
    }
}
