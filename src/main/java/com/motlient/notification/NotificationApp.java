package com.motlient.notification;

import com.motlient.notification.dto.NotificationResponse;
import com.motlient.notification.exceptions.AppValidationException;
import com.motlient.notification.processor.*;
import com.motlient.notification.util.AppValidator;

import java.util.logging.Logger;

/**
 * The NotificationApp class is the entry point for the notification processing application.
 * It initializes dependencies, validates input, processes notification requests, and handles exceptions.
 *
 * Dependencies:
 * - AppValidator: Validates the input file path.
 * - JsonParser: Parses JSON data (not used in this snippet).
 * - HttpClientWrapper: Handles HTTP communication (not used in this snippet).
 * - NotificationProcessor: Sends notifications and processes the response.
 *
 * The main method sets up the application and initiates the processing with provided arguments.
 */
public class NotificationApp {

    public static final Logger LOGGER = Logger.getLogger(NotificationApp.class.getName());
    private final AppValidator appValidator;
    private final JsonParser jsonParser;
    private final HttpClientWrapper httpClientWrapper;
    private final NotificationProcessor processor;

    public NotificationApp(AppValidator appValidator, JsonParser jsonParser, HttpClientWrapper httpClientWrapper,
                           NotificationProcessor processor) {
        this.appValidator = appValidator;
        this.jsonParser = jsonParser;
        this.httpClientWrapper = httpClientWrapper;
        this.processor = processor;
    }

    public static void main(String[] args) {
        NotificationAppFactory factory = new NotificationAppFactory();
        NotificationApp notificationApp = factory.createNotificationApp();
        notificationApp.process(args);
    }

    private void process(String[] args) {
        try {
            //1. Validating input argument
            appValidator.validateJsonFilePath(args);

            //2. Process the file
            NotificationResponse response = processor.sendNotification(args[0]);

            //3. Printing the response
            System.out.println(response.toString());
            LOGGER.info(response.toString());

        } catch (AppValidationException exception) {
            LOGGER.severe("Validation Exception : " + exception.getMessage());
            System.exit(1);

        } catch (Exception exception) {
            LOGGER.severe("Exception Caught: " + exception.getMessage());
            System.exit(1);
        }
    }
}
