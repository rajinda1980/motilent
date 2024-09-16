package com.motlient.notification;

import com.motlient.notification.exceptions.AppValidationException;
import com.motlient.notification.processor.JsonParser;
import com.motlient.notification.processor.NotificationProcessor;
import com.motlient.notification.processor.NotificationProcessorImpl;
import com.motlient.notification.processor.WebhookJsonParser;
import com.motlient.notification.util.AppValidator;

import java.util.logging.Logger;

/**
 * Main class to run the application.
 *
 * Application summary: This application takes a JSON file as input, sends an HTTP POST request to the given notification URL,
 * and handles and logs the output, including the response time and status.
 *
 * Important:
 * 1. The application accepts only one argument.
 */
public class NotificationApp {

    public static final Logger LOGGER = Logger.getLogger(NotificationApp.class.getName());
    private final AppValidator appValidator;

    public NotificationApp(AppValidator appValidator) {
        this.appValidator = appValidator;
    }

    public static void main(String[] args) {
        NotificationApp notificationApp = new NotificationApp(new AppValidator());
        notificationApp.process(args);
    }

    public void run(String[] args) {
        process(args);
    }

    private void process(String[] args) {
        try {
            //1. Validating input argument
            appValidator.validateJsonFilePath(args);

            //2. Process the file
            JsonParser jsonParser = new WebhookJsonParser();
            NotificationProcessor processor = new NotificationProcessorImpl(jsonParser);
            processor.sendNotification(args[0]);

        } catch (AppValidationException exception) {
            LOGGER.severe("Validation Exception : " + exception.getMessage());
            System.exit(1);

        } catch (Exception exception) {
            LOGGER.severe("Exception Caught: " + exception.getMessage());
            System.exit(1);
        }
    }
}
