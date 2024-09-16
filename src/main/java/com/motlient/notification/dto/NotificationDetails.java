package com.motlient.notification.dto;

public class NotificationDetails {
    private String notificationUrl;
    private Object notificationContent;

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    public Object getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(Object notificationContent) {
        this.notificationContent = notificationContent;
    }
}
