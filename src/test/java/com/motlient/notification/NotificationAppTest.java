package com.motlient.notification;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

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

    @DisplayName("Application should be terminated when no argument is provided")
    @Test
    void shouldTerminateWhenNoArgumentsProvided() throws Exception {
        int exitStatus = SystemLambda.catchSystemExit(() -> NotificationApp.main(new String[]{}));
        Assertions.assertEquals(1, exitStatus, "Notification Application : File path is not provided");

        Optional<LogRecord> findMessage =
                logRecords.stream().filter(r -> r.getMessage().contains("Notification Application : File path is not provided"))
                        .findFirst();
        Assertions.assertTrue(findMessage.isPresent());
        Assertions.assertEquals("Notification Application : File path is not provided", findMessage.get().getMessage());
    }

}
