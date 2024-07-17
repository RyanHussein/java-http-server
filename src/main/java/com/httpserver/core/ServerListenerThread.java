package com.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A thread that listens for incoming connections on a specified port.
 */
public class ServerListenerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

    private final String webroot;
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;

    /**
     * Constructs a new ServerListenerThread.
     *
     * @param port the port to listen on
     * @param webroot the root directory for web content
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public ServerListenerThread(int port, String webroot) throws IOException {
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(port);
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    /**
     * Runs the server listener thread, accepting incoming connections and handling them.
     */
    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                LOGGER.info("Connection Accepted: {}", socket.getInetAddress());
                threadPool.submit(new HttpConnectionWorkerThread(socket, webroot));
            }
        } catch (IOException e) {
            LOGGER.error("Error with setting socket: ", e);
        } finally {
            closeSocket();
        }
    }

    /**
     * Closes the server socket if it is open.
     */
    private void closeSocket() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                LOGGER.error("Error closing server socket: ", e);
            }
        }
    }
}
