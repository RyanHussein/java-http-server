package com.httpserver.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTests {

    @Test
    void testSetAndGetPort() {
        Configuration config = new Configuration();

        config.setPort(8080);
        assertEquals(8080, config.getPort());

        config.setPort(9090);
        assertEquals(9090, config.getPort());
    }

    @Test
    void testSetAndGetWebroot() {
        Configuration config = new Configuration();

        config.setWebroot("/var/www");
        assertEquals("/var/www", config.getWebroot());

        config.setWebroot("/usr/local/www");
        assertEquals("/usr/local/www", config.getWebroot());
    }
}
