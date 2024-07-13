package com.httpserver.config;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationManagerTests {

    private static Path tempConfigFile;

    @BeforeAll
    static void setup() throws IOException {
        String configJson = "{ \"port\": 8080, \"webroot\": \"/var/www\" }";
        tempConfigFile = Files.createTempFile("tempConfig", ".json");
        Files.write(tempConfigFile, configJson.getBytes());
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(tempConfigFile);
    }

    @BeforeEach
    void resetConfigurationManager() {
        // Reset the singleton instance before each test
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        configurationManager.clearConfiguration();
    }

    @Test
    void testSingletonInstance() {
        ConfigurationManager instance1 = ConfigurationManager.getInstance();
        ConfigurationManager instance2 = ConfigurationManager.getInstance();
        assertSame(instance1, instance2, "Instances are not the same");
    }

    @Test
    void testLoadConfigurationFile() {
        ConfigurationManager instance = ConfigurationManager.getInstance();
        instance.loadConfigurationFile(tempConfigFile.toString());

        Configuration config = instance.getConfiguration();
        assertEquals(8080, config.getPort());
        assertEquals("/var/www", config.getWebroot());
    }

    @Test
    void testGetConfiguration() {
        ConfigurationManager instance = ConfigurationManager.getInstance();
        instance.loadConfigurationFile(tempConfigFile.toString());
        Configuration config = instance.getConfiguration();
        assertNotNull(config, "Configuration should not be null");
        assertEquals(8080, config.getPort());
        assertEquals("/var/www", config.getWebroot());
    }

    @Test
    void testLoadConfigurationFileNotFound() {
        ConfigurationManager instance = ConfigurationManager.getInstance();
        assertThrows(HttpConfigurationException.class, () -> instance.loadConfigurationFile("nonexistent.json"));
    }

    @Test
    void testGetConfigurationNotSet() {
        ConfigurationManager instance = ConfigurationManager.getInstance();
        // Ensure configuration is not set
        instance.clearConfiguration();
        assertThrows(HttpConfigurationException.class, instance::getConfiguration);
    }
}
