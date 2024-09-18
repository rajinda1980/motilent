package com.motlient.notification;

import com.motlient.notification.processor.*;
import com.motlient.notification.util.AppValidator;

/**
 * Factory class for creating instances of the {@link NotificationApp}.
 * This class encapsulates the creation and configuration of all dependencies required for a {@link NotificationApp},
 * including validation, JSON parsing, and HTTP client functionality, ensuring that the {@link NotificationApp} is properly initialized.
 */
public class NotificationAppFactory {

    public NotificationApp createNotificationApp() {
        AppValidator appValidator = new AppValidator();
        JsonParser jsonParser = new WebhookJsonParser();
        HttpClientWrapper clientWrapper = new WebhookHttpClient();
        NotificationProcessor processor = new NotificationProcessorImpl(jsonParser, appValidator, clientWrapper);
        return new NotificationApp(appValidator, jsonParser, clientWrapper, processor);
    }
}
