package com.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Json class.
 */
class JsonTests {

    @Test
    void testToJsonNodeFromString() throws JsonProcessingException {
        String jsonString = "{\"name\":\"John\",\"age\":30}";
        JsonNode jsonNode = Json.toJsonNode(jsonString);

        assertNotNull(jsonNode);
        assertEquals("John", jsonNode.get("name").asText());
        assertEquals(30, jsonNode.get("age").asInt());
    }

    @Test
    void testToPOJOFromJsonNode() throws JsonProcessingException {
        String jsonString = "{\"name\":\"John\",\"age\":30}";
        JsonNode jsonNode = Json.toJsonNode(jsonString);
        TestPOJO testPOJO = Json.toPOJO(jsonNode, TestPOJO.class);

        assertNotNull(testPOJO);
        assertEquals("John", testPOJO.getName());
        assertEquals(30, testPOJO.getAge());
    }

    @Test
    void testToJsonNodeFromPOJO() {
        TestPOJO testPOJO = new TestPOJO("John", 30);
        JsonNode jsonNode = Json.toJsonNode(testPOJO);

        assertNotNull(jsonNode);
        assertEquals("John", jsonNode.get("name").asText());
        assertEquals(30, jsonNode.get("age").asInt());
    }

    @Test
    void testStringify() throws JsonProcessingException {
        TestPOJO testPOJO = new TestPOJO("John", 30);
        JsonNode jsonNode = Json.toJsonNode(testPOJO);
        String jsonString = Json.stringify(jsonNode);

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("\"name\":\"John\""));
        assertTrue(jsonString.contains("\"age\":30"));
    }

    @Test
    void testStringifyPretty() throws JsonProcessingException {
        TestPOJO testPOJO = new TestPOJO("John", 30);
        JsonNode jsonNode = Json.toJsonNode(testPOJO);
        String jsonString = Json.stringifyPretty(jsonNode);

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("\"name\" : \"John\""));
        assertTrue(jsonString.contains("\"age\" : 30"));
    }
}