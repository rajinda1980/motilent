package com.motlient.notification.dto;

public class NotificationResponse {
    private final String notificationUrl;
    private final String contentSent;
    private final String responseReceived;
    private final int responseCode;
    private final long responseTime;

    public NotificationResponse(String notificationUrl, String contentSent, String responseReceived, int responseCode, long responseTime) {
        this.notificationUrl = notificationUrl;
        this.contentSent = contentSent;
        this.responseReceived = responseReceived;
        this.responseCode = responseCode;
        this.responseTime = responseTime;
    }

    public String getNotificationUrl() { return notificationUrl; }
    public String getContentSent() { return contentSent; }
    public String getResponseReceived() { return responseReceived; }
    public int getResponseCode() { return responseCode; }
    public long getResponseTime() { return responseTime; }

    public String toString() {
        return
                "Notification URL: " + notificationUrl + "\n" +
                        "Content Sent: " + contentSent + "\n" +
                        "Response Received: " + responseReceived + "\n" +
                        "HTTP Response Code: " + responseCode + "\n" +
                        "Response Time: " + responseTime + " ms";
    }
}
