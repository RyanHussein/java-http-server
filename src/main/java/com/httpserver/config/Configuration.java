package com.httpserver.config;

/**
 * Represents the server configuration.
 */
public class Configuration {

    private int port;
    private String webroot;

    /**
     * Gets the port number for the server.
     *
     * @return the port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port number for the server.
     *
     * @param port the port number to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets the web root directory for the server.
     *
     * @return the web root directory
     */
    public String getWebroot() {
        return webroot;
    }

    /**
     * Sets the web root directory for the server.
     *
     * @param webroot the web root directory to set
     */
    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }
}
