package com.httpserver.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.httpserver.config.HttpConfigurationException.HttpConfigurationException;
import com.httpserver.util.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Singleton class for managing the server configuration.
 */
public class ConfigurationManager {

    private static ConfigurationManager configurationManager;
    private static Configuration configuration;

    /**
     * Private constructor to prevent instantiation.
     */
    private ConfigurationManager() {}

    /**
     * Returns the singleton instance of ConfigurationManager.
     *
     * @return the singleton instance
     */
    public static synchronized ConfigurationManager getInstance() {
        if (configurationManager == null) {
            configurationManager = new ConfigurationManager();
        }
        return configurationManager;
    }

    /**
     * Loads the configuration file and parses it into a Configuration object.
     *
     * @param filePath the path to the configuration file
     */
    public void loadConfigurationFile(String filePath) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonNode configNode = Json.toJsonNode(json);
            configuration = Json.toPOJO(configNode, Configuration.class);
        } catch (IOException e) {
            throw new HttpConfigurationException("Error loading or parsing the configuration file: " + filePath, e);
        }
    }

    /**
     * Returns the current configuration.
     *
     * @return the current configuration
     * @throws HttpConfigurationException if the configuration has not been set
     */
    public Configuration getConfiguration() {
        if (configuration == null) {
            throw new HttpConfigurationException("Configuration not set.");
        }
        return configuration;
    }
}
