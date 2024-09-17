package com.motlient.notification.dto;

import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NotificationResponseTest {

    private NotificationResponse notificationResponse;

    @BeforeEach
    void init() {

    }
    @DisplayName("Test for class constructor and getters")
    @Test
    void shouldReturnValueSetInConstructor() {
        String notificationUrl = "https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb";
        String notificationContent =  "{\"reportUID\":\"20fb8e02-9c55-410a-93a9-489c6f1d7598\",\"studyInstanceUID\":\"9998e02-9c55-410a-93a9-489c6f789798\"}";
        String responseReceived = "{\"reportUID\":\"20fb8e02-9c55-410a-93a9-489c6f1d7598\",\"studyInstanceUID\":\"9998e02-9c55-410a-93a9-489c6f789798\"}";
        int responseCode = 200;
        long responseTime = 175;

        NotificationResponse notificationResponse = new NotificationResponse(notificationUrl, notificationContent, responseReceived, responseCode, responseTime);

        Assertions.assertEquals(notificationUrl, notificationResponse.getNotificationUrl());
        Assertions.assertEquals(notificationContent, notificationResponse.getContentSent());
        Assertions.assertEquals(responseReceived, notificationResponse.getResponseReceived());
        Assertions.assertEquals(responseCode, notificationResponse.getResponseCode());
        Assertions.assertEquals(responseTime, notificationResponse.getResponseTime());
    }

    @DisplayName("Test for toString method of the class")
    @Test
    void shouldReturnValueSetToFieldsfromThetoString() {
        String notificationUrl = "https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb";
        String notificationContent =  "{\"reportUID\":\"20fb8e02-9c55-410a-93a9-489c6f1d7598\",\"studyInstanceUID\":\"9998e02-9c55-410a-93a9-489c6f789798\"}";
        String responseReceived = "{\"reportUID\":\"20fb8e02-9c55-410a-93a9-489c6f1d7598\",\"studyInstanceUID\":\"9998e02-9c55-410a-93a9-489c6f789798\"}";
        int responseCode = 200;
        long responseTime = 175;

        NotificationResponse notificationResponse = new NotificationResponse(notificationUrl, notificationContent, responseReceived, responseCode, responseTime);

        String expectedString = """
                Notification URL: https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb
                Content Sent: {"reportUID":"20fb8e02-9c55-410a-93a9-489c6f1d7598","studyInstanceUID":"9998e02-9c55-410a-93a9-489c6f789798"}
                Response Received: {"reportUID":"20fb8e02-9c55-410a-93a9-489c6f1d7598","studyInstanceUID":"9998e02-9c55-410a-93a9-489c6f789798"}
                HTTP Response Code: 200
                Response Time: 175 ms""";
        Assertions.assertEquals(expectedString, notificationResponse.toString());
    }

    @DisplayName("Test for status code boundary values")
    @Test
    void testForStatusCodeBoundaryValues() {
        NotificationResponse notificationResponse1 = new NotificationResponse("https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb", "content", "response", 404, 200);
        NotificationResponse notificationResponse2 = new NotificationResponse("https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb", "content", "response", 500, 300);
        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, notificationResponse1.getResponseCode());
        Assertions.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, notificationResponse2.getResponseCode());
    }

    @DisplayName("Test for response time value")
    @Test
    void shouldReturnResponseTimeZero() {
        NotificationResponse notificationResponse1 = new NotificationResponse("https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb", "content", "response", 200, 0);
        Assertions.assertEquals(0, notificationResponse1.getResponseTime());
    }
}
