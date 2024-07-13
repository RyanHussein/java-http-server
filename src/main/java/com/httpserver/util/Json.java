package com.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

/**
 * Utility class for handling JSON serialisation and deserialisation using Jackson.
 */
public class Json {

    private static final ObjectMapper objectMapper = getObjectMapper();

    /**
     * Creates and configures an ObjectMapper instance.
     *
     * @return a configured ObjectMapper instance
     */
    private static ObjectMapper getObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        configureObjectMapper(om);
        return om;
    }

    /**
     * Configures the provided ObjectMapper.
     *
     * @param om the ObjectMapper to configure
     */
    private static void configureObjectMapper(ObjectMapper om) {
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Converts a JSON string into a JsonNode.
     *
     * @param jsonSrc the JSON string to convert
     * @return a JsonNode representing the JSON string
     * @throws JsonProcessingException if the JSON string cannot be processed
     */
    public static JsonNode toJsonNode(String jsonSrc) throws JsonProcessingException {
        return objectMapper.readTree(jsonSrc);
    }

    /**
     * Converts a JsonNode into a POJO.
     *
     * @param <A> the type of the POJO
     * @param node the JsonNode to convert
     * @param clazz the class of the POJO
     * @return the POJO represented by the JsonNode
     * @throws JsonProcessingException if the JsonNode cannot be converted
     */
    public static <A> A toPOJO(JsonNode node, Class<A> clazz) throws JsonProcessingException {
        return objectMapper.treeToValue(node, clazz);
    }

    /**
     * Converts a POJO into a JsonNode.
     *
     * @param obj the POJO to convert
     * @return a JsonNode representing the POJO
     */
    public static JsonNode toJsonNode(Object obj) {
        return objectMapper.valueToTree(obj);
    }

    /**
     * Converts a JsonNode into a JSON string.
     *
     * @param node the JsonNode to convert
     * @return a JSON string representing the JsonNode
     * @throws JsonProcessingException if the JsonNode cannot be converted
     */
    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateString(node, false);
    }

    /**
     * Converts a JsonNode into a prettified JSON string.
     *
     * @param node the JsonNode to convert
     * @return a prettified JSON string representing the JsonNode
     * @throws JsonProcessingException if the JsonNode cannot be converted
     */
    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return generateString(node, true);
    }

    /**
     * Generates a JSON string from a JsonNode.
     *
     * @param node the JsonNode to convert
     * @param pretty whether to prettify the JSON string
     * @return a JSON string representing the JsonNode
     * @throws JsonProcessingException if the JsonNode cannot be converted
     */
    private static String generateString(JsonNode node, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = objectMapper.writer();
        if (pretty)
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        return objectWriter.writeValueAsString(node);
    }
}

