package com.motlient.notification;

import com.motlient.notification.dto.NotificationResponse;
import com.motlient.notification.exceptions.AppValidationException;
import com.motlient.notification.processor.*;
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
    private final JsonParser jsonParser;
    private final HttpClientWrapper httpClientWrapper;

    public NotificationApp(AppValidator appValidator, JsonParser jsonParser, HttpClientWrapper httpClientWrapper) {
        this.appValidator = appValidator;
        this.jsonParser = jsonParser;
        this.httpClientWrapper = httpClientWrapper;
    }

    public static void main(String[] args) {
        NotificationApp notificationApp = new NotificationApp(new AppValidator(), new WebhookJsonParser(), new WebhookHttpClient());
        notificationApp.process(args);
    }

    private void process(String[] args) {
        try {
            //1. Validating input argument
            appValidator.validateJsonFilePath(args);

            //2. Process the file
            NotificationProcessor processor = new NotificationProcessorImpl(jsonParser, appValidator, httpClientWrapper);
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
