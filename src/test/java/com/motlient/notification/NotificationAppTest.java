package com.motlient.notification;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import com.motlient.notification.dto.NotificationResponse;
import com.motlient.notification.processor.HttpClientWrapper;
import com.motlient.notification.processor.JsonParser;
import com.motlient.notification.processor.NotificationProcessor;
import com.motlient.notification.util.AppValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
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
    private NotificationApp notificationApp;

    @Mock
    private AppValidator appValidator;
    @Mock
    private JsonParser jsonParser;
    @Mock
    private HttpClientWrapper httpClientWrapper;
    @Mock
    private NotificationProcessor processor;
    @Mock
    private NotificationResponse notificationResponse;

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

        MockitoAnnotations.openMocks(this);
        notificationApp = new NotificationApp(appValidator, jsonParser, httpClientWrapper, processor);
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

    @DisplayName("Application should be executed for valid input argument")
    @Test
    void shouldRunProgramForValidArgument() throws Exception {
        String[] args = {"src/test/resources/valid_notification.json"};
        String response = """
                Notification URL: https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb
                Content Sent: {"reportUID":"20fb8e02-9c55-410a-93a9-489c6f1d7598","studyInstanceUID":"9998e02-9c55-410a-93a9-489c6f789798"}
                Response Received: {"reportUID":"20fb8e02-9c55-410a-93a9-489c6f1d7598","studyInstanceUID":"9998e02-9c55-410a-93a9-489c6f789798"}
                HTTP Response Code: 200
                Response Time: 175 ms""";

        Mockito.doNothing().when(appValidator).validateJsonFilePath(args);
        Mockito.when(processor.sendNotification(args[0])).thenReturn(notificationResponse);
        Mockito.when(notificationResponse.toString()).thenReturn(response);

        Method processMethod = NotificationApp.class.getDeclaredMethod("process", String[].class);
        processMethod.setAccessible(true);
        processMethod.invoke(notificationApp, (Object) args);

        Mockito.verify(appValidator).validateJsonFilePath(args);
        Mockito.verify(processor).sendNotification(args[0]);
    }
}
