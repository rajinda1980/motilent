package com.motlient.notification.util;

import com.motlient.notification.exceptions.AppValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
                Arguments.of("Too many arguments provided. Multiple file paths are not allowed", new String[]{"path1", "path2"}),
                Arguments.of("Invalid file path", new String[]{"https://www.motlient.com/invalid$json#file*path"}),
                Arguments.of("Invalid file path", new String[]{"  https://www.motlient.com/valid-json-file-path  "}),
                Arguments.of("File path length cannot exceed 200 characters", new String[]{"https://www.motlient.com/api/v1/resources/search?query=example-long-query-string-for-testing-the-maximum-length-of-urls-in-various-browsers-and-systems&filter=category&type=json&sort=ascending&limit=100&offset=0&additionalParameter1=value1&additionalParameter2=value2"}),
                Arguments.of("Invalid URL format", new String[]{"htmp://www.motlient.com/valid-file-name"}),
                Arguments.of("Invalid file path", new String[]{"http://www.motlient.com/<script>alert('test')</script>"})
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

    /**
     * Value map
     * @return argument list
     */
    static Stream<Arguments> shouldNotThrowAnExceptionForValidUrl() {
        return Stream.of(
                Arguments.of((Object) new String[]{"https://www.motlient.com/api/v1/resources?query=value&another_query=value@123~name-abc"}),
                Arguments.of((Object) new String[]{"https://www.motlient.com/api/v1/resources?query=value"}),
                Arguments.of((Object) new String[]{"http://www.motlient.com/valid-file-path"}),
                Arguments.of((Object) new String[]{"https://www.motlient.com/valid-file-path"}),
                Arguments.of((Object) new String[]{"ftp://www.motlient.com/valid-file-path"})
        );
    }

    @DisplayName("Exception should not be thrown for valid url")
    @ParameterizedTest
    @MethodSource
    void shouldNotThrowAnExceptionForValidUrl(String[] args) {
        AppValidator validator = new AppValidator();
        Assertions.assertDoesNotThrow(() -> validator.validateJsonFilePath(args));
    }
}
