package com.motlient.notification.processor;

import com.motlient.notification.dto.NotificationDetails;
import com.motlient.notification.dto.NotificationResponse;
import com.motlient.notification.exceptions.AppValidationException;
import com.motlient.notification.util.AppConstants;
import com.motlient.notification.util.AppValidator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * Implementation of the NotificationProcessor interface.
 *
 * This class handles the process of sending notifications by:
 * 1. Parsing notification details from a JSON file.
 * 2. Validating the notification URL.
 * 3. Sending an HTTP POST request with the notification content.
 * 4. Measuring and returning the response time and details.
 *
 * It uses a JsonParser for parsing and serializing JSON, an AppValidator for URL validation, and an HttpClientWrapper for sending HTTP requests.
 */
public class NotificationProcessorImpl implements NotificationProcessor {

    private static final Logger LOGGER = Logger.getLogger(NotificationProcessorImpl.class.getName());

    private final JsonParser jsonParser;
    private final AppValidator validator;
    private final HttpClientWrapper clientWrapper;

    public NotificationProcessorImpl(JsonParser jsonParser, AppValidator validator, HttpClientWrapper clientWrapper) {
        this.jsonParser = jsonParser;
        this.validator = validator;
        this.clientWrapper = clientWrapper;
    }

    public NotificationResponse sendNotification(String filePath) throws Exception {
        try {
            NotificationDetails details = jsonParser.parse(filePath, NotificationDetails.class);
            String content = jsonParser.serialize(details.getNotificationContent());

            String notificationUrl = details.getNotificationUrl();
            LOGGER.info("Notification URL : " + notificationUrl);

            if (validator.validateUrlForValidUrlPatten(notificationUrl)) {
                HttpRequest request =
                        HttpRequest.newBuilder()
                                .uri(URI.create(notificationUrl))
                                .header("Content-Type", AppConstants.CONTENT_TYPE_APPLICATION_JSON)
                                .POST(HttpRequest.BodyPublishers.ofString(content))
                                .build();


                return getNotificationResponse(details, content, request);

            } else {
                LOGGER.severe("An invalid notification URL is provided. URL : " + notificationUrl);
                throw new AppValidationException(AppConstants.MESSAGE_INVALID_NOTIFICATION_URL);
            }

        } catch (IOException exception) {
            LOGGER.severe("Json Processing Exception : " + exception.getMessage());
            throw new Exception(exception.getMessage());

        } catch (Exception exception) {
            LOGGER.severe("Processing Exception : " + exception.getMessage());
            throw new Exception(exception.getMessage());
        }
    }

    private NotificationResponse getNotificationResponse(NotificationDetails details, String content, HttpRequest request) throws Exception {
        Instant start = Instant.now();
        HttpResponse<String> response = clientWrapper.sendRequest(request);
        Instant end = Instant.now();

        long responseTimeMillis = Duration.between(start, end).toMillis();
        String responseBody = (null != response.body() ? response.body().replaceAll("\\s+", "") : "");

        return new NotificationResponse(
                        details.getNotificationUrl(),
                        content,
                        responseBody,
                        response.statusCode(),
                        responseTimeMillis);
    }
}
