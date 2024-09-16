package com.motlient.notification.processor;

import com.motlient.notification.dto.NotificationDetails;

import java.io.IOException;
import java.util.logging.Logger;

public class NotificationProcessorImpl implements NotificationProcessor {

    private static final Logger LOGGER = Logger.getLogger(NotificationProcessorImpl.class.getName());

    private final JsonParser jsonParser;

    public NotificationProcessorImpl(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public void sendNotification(String filePath) throws Exception {
        try {
            NotificationDetails details = jsonParser.parse(filePath, NotificationDetails.class);
            String content = jsonParser.serialize(details.getNotificationContent());

            System.out.println(details.getNotificationUrl());

        } catch (IOException exception) {
            LOGGER.severe("Json Processing Exception : " + exception.getMessage());
            throw new Exception(exception.getMessage());

        } catch (Exception exception) {
            LOGGER.severe("Processing Exception : " + exception.getMessage());
            throw new Exception(exception.getMessage());
        }
    }
}
