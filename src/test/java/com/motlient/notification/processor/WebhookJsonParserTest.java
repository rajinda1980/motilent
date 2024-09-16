package com.motlient.notification.processor;

import com.motlient.notification.dto.NotificationDetails;
import com.motlient.notification.util.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

public class WebhookJsonParserTest {

    private WebhookJsonParser parser;

    static class TestContent {
        public String reportUID;
        public String studyInstanceUID;

        public TestContent() {}
    }

    static class Wrapper {
        public String id;
        public String name;
        public TestContent testContent;

        public Wrapper() {}
    }

    @BeforeEach
    void setUp() {
        parser = new WebhookJsonParser();
    }

    @DisplayName("Test for successful parsing of a valid JSON file")
    @Test
    void shouldGenerateNotificationDetails() throws Exception {
        NotificationDetails details = parser.parse(TestConstants.VALID_NOTIFICATION_JSON, NotificationDetails.class);
        Assertions.assertNotNull(details);
        Assertions.assertEquals("https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb", details.getNotificationUrl());

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) details.getNotificationContent();
        Assertions.assertEquals("20fb8e02-9c55-410a-93a9-489c6f1d7598", map.get("reportUID"));
        Assertions.assertEquals("9998e02-9c55-410a-93a9-489c6f789798", map.get("studyInstanceUID"));
    }

    /**
     * Value map
     * @return argument list
     */
    static Stream<Arguments> shouldGenerateIOException() {
        return Stream.of(
                Arguments.of("file_does_not_exist_path", "file_does_not_exist_path (No such file or directory"),            // Test for parsing a non-existent file
                Arguments.of(TestConstants.MALFORMED_NOTIFICATION_JSON, "was expecting comma to separate Object entries"),  //  Test for parsing a malformed JSON file
                Arguments.of(TestConstants.EMPTY_NOTIFICATION_JSON, "No content to map due to end-of-input")                // Test for parsing an empty JSON file
        );
    }

    @DisplayName("Testing edge cases")
    @ParameterizedTest
    @MethodSource
    void shouldGenerateIOException(String filePath, String expected) {
        IOException exception = Assertions.assertThrows(IOException.class,
                () -> parser.parse(filePath, NotificationDetails.class));
        Assertions.assertTrue(exception.getMessage().contains(expected));
    }

    @DisplayName("Test for serializing a valid json object")
    @Test
    void shouldSerializeValidNotification() throws Exception {
        TestContent testContent = new TestContent();
        testContent.reportUID = "20fb8e02-9c55-410a-93a9-489c6f1d7598";
        testContent.studyInstanceUID = "9998e02-9c55-410a-93a9-489c6f789798";

        String json = parser.serialize(testContent);
        Assertions.assertNotNull(json);
        Assertions.assertTrue(json.contains("\"reportUID\":\"20fb8e02-9c55-410a-93a9-489c6f1d7598\""));
        Assertions.assertTrue(json.contains("\"studyInstanceUID\":\"9998e02-9c55-410a-93a9-489c6f789798\""));
    }

    @DisplayName("Test for serializing a null object")
    @Test
    void shouldReturnNullValueForNullObject() throws Exception {
        String result = parser.serialize(null);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("null", result);
    }

    @DisplayName("Test for serializing an content with null fields")
    @Test
    void shouldReturnContentWithNullField() throws Exception {
        TestContent testContent = new TestContent();
        String result = parser.serialize(testContent);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.contains("\"reportUID\":null"));
        Assertions.assertTrue(result.contains("\"studyInstanceUID\":null"));
    }

    @DisplayName("Test for serializing an content which is enclosed in another class")
    @Test
    void shouldReturnWrapper() throws Exception {
        Wrapper wrapper = new Wrapper();
        wrapper.id = "NID:001";
        wrapper.name = "notification";
        wrapper.testContent = new TestContent();

        String result = parser.serialize(wrapper);
        Assertions.assertNotNull(wrapper);
        Assertions.assertTrue(result.contains("\"id\":\"NID:001\""));
        Assertions.assertTrue(result.contains("\"name\":\"notification\""));
        Assertions.assertTrue(result.contains("\"testContent\":{\"reportUID\":null,\"studyInstanceUID\":null"));
    }
}
