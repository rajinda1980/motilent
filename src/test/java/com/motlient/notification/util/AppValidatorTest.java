package com.motlient.notification.util;

import com.motlient.notification.exceptions.AppValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class AppValidatorTest {

    /**
     * Value map
     * @return argument list
     */
    static Stream<Arguments> shouldThrowAnExceptionForInvalidUrl() {
        return Stream.of(
                Arguments.of("File path is not provided", null),
                Arguments.of("File path is not provided", new String[]{""}),
                Arguments.of("File path is not provided", new String[]{}),
                Arguments.of("Too many arguments provided. Multiple file paths are not allowed", new String[]{"path1", "path2"})
        );
    }

    @DisplayName("Exception should be thrown for invalid url")
    @ParameterizedTest
    @MethodSource
    void shouldThrowAnExceptionForInvalidUrl(String expect, String[] input) {
        AppValidator appValidator = new AppValidator();
        AppValidationException exception = Assertions.assertThrows(AppValidationException.class, () -> appValidator.validateJsonFilePath(input));
        Assertions.assertEquals(expect, exception.getMessage());
    }

    @DisplayName("Exception should not be thrown for valid url")
    @Test
    void shouldNotThrowAnExceptionForValidUrl() {
        AppValidator validator = new AppValidator();
        String[] args = {"src/test/resources/notification.json"};
        Assertions.assertDoesNotThrow(() -> validator.validateJsonFilePath(args));
    }
}
