package com.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * A thread that handles an individual HTTP connection.
 */
public class HttpConnectionWorkerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    private final Socket socket;

    /**
     * Constructs a new HttpConnectionWorkerThread.
     *
     * @param socket the socket connected to the client
     */
    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    /**
     * Runs the thread to process the HTTP connection.
     */
    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {

            // Hardcoded HTML response
            String html = "<html><head><title>HI</title></head><body><h1>HI</h1></body></html>";
            final String CRLF = "\r\n";

            // Construct HTTP response
            String response = "HTTP/1.1 200 OK" + CRLF +
                    "Content-Length: " + html.getBytes().length + CRLF + CRLF +
                    html + CRLF + CRLF;

            // Send response
            outputStream.write(response.getBytes());

            LOGGER.info("Connection processing finished.");
        } catch (IOException e) {
            LOGGER.error("Error with communication: ", e);
        } finally {
            closeSocket();
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
