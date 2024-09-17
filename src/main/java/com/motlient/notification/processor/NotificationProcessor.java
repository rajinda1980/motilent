package com.motlient.notification.processor;

import com.motlient.notification.dto.NotificationResponse;

public interface NotificationProcessor {
    NotificationResponse sendNotification(String filePath) throws Exception;
}
