package com.httpserver;

import com.httpserver.config.Configuration;
import com.httpserver.config.ConfigurationManager;
import com.httpserver.core.ServerListenerThread;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import org.slf4j.Logger;

/**
 * The main class for the HTTP server.
 */
public class HttpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    /**
     * The entry point for the HTTP server application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Server Starting...");

        // Load the configuration file
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration config = ConfigurationManager.getInstance().getConfiguration();

        LOGGER.info("Using port: {}", config.getPort());
        LOGGER.info("Using webroot: {}", config.getWebroot());

        // Start the server listener thread
        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(config.getPort(), config.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            LOGGER.error("Error starting the server", e);
        }
    }
}
