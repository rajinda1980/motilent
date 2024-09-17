package com.motlient.notification.util;

import com.motlient.notification.exceptions.AppValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class AppValidatorTest {

    private AppValidator appValidator;

    @BeforeEach
    void init() {
        appValidator = new AppValidator();
    }

    /**
     * Value map
     * @return argument list
     */
    static Stream<Arguments> shouldThrowAnExceptionForInvalidFilePath() {
        return Stream.of(
                Arguments.of("File path is not provided", null),                // Test for validating null file path
                Arguments.of("File path is not provided", new String[]{""}),    // Test for validating empty file path
                Arguments.of("File path is not provided", new String[]{}),      // Test for validating empty file path
                Arguments.of("Too many arguments provided. Multiple file paths are not allowed", new String[]{"path1", "path2"})    // Test for validating multiple file paths
        );
    }

    @DisplayName("Test edge cases for invalid file path")
    @ParameterizedTest
    @MethodSource
    void shouldThrowAnExceptionForInvalidFilePath(String expect, String[] input) {
        AppValidationException exception = Assertions.assertThrows(AppValidationException.class, () -> appValidator.validateJsonFilePath(input));
        Assertions.assertEquals(expect, exception.getMessage());
    }

    @DisplayName("Test for valid file path")
    @Test
    void shouldNotThrowAnExceptionForValidFilePath() {
        String[] args = {"src/test/resources/notification.json"};
        Assertions.assertDoesNotThrow(() -> appValidator.validateJsonFilePath(args));
    }

    @DisplayName("Test for valid notification url")
    @Test
    void shouldReturnTrueForValidNotificationUrl() {
        String url = "https://webhook.site/d9d87d20-6729-4992-b986-f7e0776ae234";
        boolean result = appValidator.validateUrlForValidUrlPatten(url);
        Assertions.assertTrue(result);
    }

    /**
     * Value map
     * @return argument list
     */
    static Stream<Arguments> shouldReturnFalseForInvalidNotificationUrl() {
        return Stream.of(
                Arguments.of("http://webhook.site/d9d87d20-6729-4992-b986-f7e0776ae234"),
                Arguments.of("ftp://webhook.site/d9d87d20-6729-4992-b986-f7e0776ae234"),
                Arguments.of("https://webhook.site.co.uk/d9d87d20-6729-4992-b986-f7e0776ae234"),
                Arguments.of("http://webhook-site/d9d87d20-6729-4992-b986-f7e0776ae234"),
                Arguments.of("https://webhook.site/#%$$^d9d87d20-6729-4992-b986-f7e0776ae234"),
                Arguments.of("https://webhook.site/d9d87d20-6729-4992/b986/f7e0776ae234"),
                Arguments.of("https://webhook.site"),
                Arguments.of("https://webhook.site/some-path?query=param"),
                Arguments.of("https://google.com"),
                Arguments.of("webhook.site/d9d87d20-6729-4992-b986-f7e0776ae234"),
                Arguments.of("https://webhook.site/d9d87d20 6729 4992-b986-f7e0776ae234"),
                Arguments.of("")
        );
    }

    @DisplayName("Test for invalid notification url")
    @ParameterizedTest
    @MethodSource
    void shouldReturnFalseForInvalidNotificationUrl(String url) {
        boolean result = appValidator.validateUrlForValidUrlPatten(url);
        Assertions.assertFalse(result);
    }
}
