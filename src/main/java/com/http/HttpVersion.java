package com.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enum representing HTTP versions.
 */
public enum HttpVersion {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    /**
     * Constructs an HttpVersion enum constant.
     *
     * @param LITERAL the string representation of the HTTP version
     * @param MAJOR the major version number
     * @param MINOR the minor version number
     */
    HttpVersion(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    private static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+)\\.(?<minor>\\d+)");

    /**
     * Returns the best compatible HTTP version for the given string representation.
     *
     * @param literalVersion the string representation of the HTTP version
     * @return the best compatible HttpVersion
     * @throws HttpParsingException if the given string representation does not match any supported HTTP version
     */
    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws HttpParsingException {
        if (literalVersion == null) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }

        Matcher matcher = httpVersionRegexPattern.matcher(literalVersion);

        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }

        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));

        HttpVersion bestCompatible = null;
        for (HttpVersion version : HttpVersion.values()) {
            if (version.LITERAL.equals(literalVersion)) {
                return version; // Exact match
            } else if (version.MAJOR == major && version.MINOR < minor) {
                bestCompatible = version; // Best compatible version
            }
        }

        if (bestCompatible == null) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }

        return bestCompatible;
    }
}
