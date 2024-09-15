package com.motlient.notification;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import com.motlient.notification.util.AppValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class NotificationAppTest {

    private static final Logger LOGGER = Logger.getLogger(NotificationApp.class.getName());
    private List<LogRecord> logRecords;
    private Handler handler;

    @BeforeEach
    void setUp() {
        logRecords = new ArrayList<>();

        handler = new Handler() {
            @Override
            public void publish(LogRecord logRecord) {
                logRecords.add(logRecord);
            }

            @Override
            public void flush() { }

            @Override
            public void close() throws SecurityException { }
        };
        LOGGER.addHandler(handler);
        LogManager.getLogManager().addLogger(LOGGER);
    }

    @AfterEach
    void destroy() {
        handler.flush();
        LOGGER.removeHandler(handler);
        LogManager.getLogManager().reset();
    }

    /**
     * Value map
     * @return argument list
     */
    static Stream<Arguments> shouldTerminateProgram() {
        return Stream.of(
                Arguments.of(null, 1, "Validation Exception : File path is not provided"),
                Arguments.of(new String[]{""}, 1, "Validation Exception : File path is not provided"),
                Arguments.of(new String[]{}, 1, "Validation Exception : File path is not provided"),
                Arguments.of(new String[]{"path1", "path2"}, 1, "Validation Exception : Too many arguments provided. Multiple file paths are not allowed"),
                Arguments.of(new String[]{"https://www.motlient.com/invalid$json#file*path"}, 1,
                        "Validation Exception : Invalid file path"),
                Arguments.of(new String[]{"  https://www.motlient.com/valid-json-file-path  "}, 1,
                        "Validation Exception : Invalid file path"),
                Arguments.of(new String[]{"https://www.motlient.com/api/v1/resources/search?query=example-long-query-string-for-testing-the-maximum-length-of-urls-in-various-browsers-and-systems&filter=category&type=json&sort=ascending&limit=100&offset=0&additionalParameter1=value1&additionalParameter2=value2"},
                        1, "Validation Exception : File path length cannot exceed 200 characters"),
                Arguments.of(new String[]{"htmp://www.motlient.com/valid-file-name"}, 1, "Validation Exception : Invalid URL format"),
                Arguments.of(new String[]{"http://www.motlient.com/<script>alert('test')</script>"}, 1, "Validation Exception : Invalid file path")
        );
    }

    @DisplayName("Application should be terminated for invalid input argument(s)")
    @ParameterizedTest
    @MethodSource
    void shouldTerminateProgram(String[] input, int expectedStatus, String expectedMessaged) throws Exception {
        int exitStatus = SystemLambda.catchSystemExit(() -> NotificationApp.main(input));
        Assertions.assertEquals(expectedStatus, exitStatus, expectedMessaged);

        Optional<LogRecord> findMessage = logRecords.stream().filter(r -> r.getMessage().contains(expectedMessaged)).findFirst();
        Assertions.assertTrue(findMessage.isPresent());
        Assertions.assertEquals(expectedMessaged, findMessage.get().getMessage());
    }

    /**
     * Value map
     * @return argument list
     */
    static Stream<Arguments> shouldExecuteProgram() {
        return Stream.of(
                Arguments.of("https://www.motlient.com/api/v1/resources?query=value&another_query=value@123~name-abc"),
                Arguments.of("https://www.motlient.com/api/v1/resources?query=value"),
                Arguments.of("http://www.motlient.com/valid-file-path"),
                Arguments.of("https://www.motlient.com/valid-file-path"),
                Arguments.of("ftp://www.motlient.com/valid-file-path")
        );
    }

    @DisplayName("Application should be executed for valid url")
    @ParameterizedTest
    @MethodSource
    void shouldExecuteProgram(String url) {
        AppValidator mockValidator = Mockito.mock(AppValidator.class);
        NotificationApp app = new NotificationApp(mockValidator);
        String[] args = {url};

        app.run(args);
        Mockito.verify(mockValidator, Mockito.times(1)).validateJsonFilePath(args);
    }

}
