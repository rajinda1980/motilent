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
                Arguments.of(new String[]{"path1", "path2"}, 1, "Validation Exception : Too many arguments provided. Multiple file paths are not allowed")
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

    @DisplayName("Application should be executed for valid url")
    @Test
    void shouldExecuteProgram() {
        AppValidator mockValidator = Mockito.mock(AppValidator.class);
        NotificationApp app = new NotificationApp(mockValidator);
        String[] args = {"src/test/resources/valid_notification.json"};

        app.run(args);
        Mockito.verify(mockValidator, Mockito.times(1)).validateJsonFilePath(args);
    }

}
