package com.motlient.notification.processor;

public interface NotificationProcessor {
    void sendNotification(String filePath) throws Exception;
}
