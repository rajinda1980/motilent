package com.motlient.notification.processor;

import com.motlient.notification.dto.NotificationDetails;
import com.motlient.notification.dto.NotificationResponse;
import com.motlient.notification.util.AppValidator;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.Mockito.*;

public class NotificationProcessorTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private AppValidator appValidator;

    @Mock
    private HttpClientWrapper httpClientWrapper;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private NotificationProcessorImpl processor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should process and send notification successfully")
    @Test
    void shouldSendNotificationSuccessfully() throws Exception {
        String filePath = "src/test/resources/valid_notification.json";
        String webhookUrl = "https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb";
        NotificationDetails notificationDetails = new NotificationDetails();
        String notificationContent =  "{\"reportUID\":\"20fb8e02-9c55-410a-93a9-489c6f1d7598\",\"studyInstanceUID\":\"9998e02-9c55-410a-93a9-489c6f789798\"}";
        notificationDetails.setNotificationUrl(webhookUrl);

        Mockito.when(jsonParser.parse(filePath, NotificationDetails.class)).thenReturn(notificationDetails);
        Mockito.when(jsonParser.serialize(notificationDetails.getNotificationContent())).thenReturn(notificationContent);
        Mockito.when(appValidator.validateUrlForValidUrlPatten(webhookUrl)).thenReturn(true);
        Mockito.when(httpResponse.body()).thenReturn(notificationContent);
        Mockito.when(httpResponse.statusCode()).thenReturn(HttpStatus.SC_OK);
        Mockito.when(httpClientWrapper.sendRequest(any(HttpRequest.class))).thenReturn(httpResponse);

        NotificationResponse response = processor.sendNotification(filePath);

        Mockito.verify(jsonParser).parse(filePath, NotificationDetails.class);
        Mockito.verify(appValidator).validateUrlForValidUrlPatten(webhookUrl);
        Mockito.verify(httpClientWrapper).sendRequest(any(HttpRequest.class));

        Assertions.assertEquals(webhookUrl, response.getNotificationUrl());
        Assertions.assertEquals(notificationContent, response.getContentSent());
        Assertions.assertEquals(notificationContent, response.getResponseReceived());
        Assertions.assertEquals(HttpStatus.SC_OK, response.getResponseCode());
        Assertions.assertTrue(response.getResponseTime() >= 0);
    }

    @DisplayName("Should throw exception for invalid notification url")
    @Test
    void shouldThrowExceptionForInvalidNotificationUrl() throws Exception {
        String filePath = "src/test/resources/invalid_notification.json";
        String webhookInvalidUrl = "https://webhook.site.co.uk";
        NotificationDetails details = new NotificationDetails();
        details.setNotificationUrl(webhookInvalidUrl);

        Mockito.when(jsonParser.parse(filePath, NotificationDetails.class)).thenReturn(details);
        Mockito.when(appValidator.validateUrlForValidUrlPatten(webhookInvalidUrl)).thenReturn(false);

        Exception exception = Assertions.assertThrows(Exception.class, () -> processor.sendNotification(filePath));

        Mockito.verify(jsonParser).parse(filePath, NotificationDetails.class);
        Mockito.verify(appValidator).validateUrlForValidUrlPatten(webhookInvalidUrl);
        Mockito.verify(httpClientWrapper, never()).sendRequest(any(HttpRequest.class));

        Assertions.assertEquals("An invalid notification URL is provided", exception.getMessage());
    }

    @DisplayName("Should throw exception during json parsing")
    @Test
    void shouldThrowExceptionDuringJsonParsing() throws Exception {
        String filePath = "src/test/resources/invalid_notification.json";
        Mockito.when(jsonParser.parse(filePath, NotificationDetails.class)).thenThrow(new IOException("Json parsing error"));
        Exception exception = Assertions.assertThrows(Exception.class, () -> processor.sendNotification(filePath));
        Mockito.verify(jsonParser).parse(filePath, NotificationDetails.class);
        Mockito.verify(httpClientWrapper, never()).sendRequest(any(HttpRequest.class));
        Assertions.assertEquals("Json parsing error", exception.getMessage());
    }

    @DisplayName("Exception should be handled while sending http request")
    @Test
    void shouldHandleExceptionDuringHttpRequest() throws Exception {
        String filePath = "src/test/resources/valid_notification.json";
        String webhookUrl = "https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb";
        NotificationDetails notificationDetails = new NotificationDetails();
        notificationDetails.setNotificationUrl(webhookUrl);
        String notificationContent =  "{\"reportUID\":\"20fb8e02-9c55-410a-93a9-489c6f1d7598\",\"studyInstanceUID\":\"9998e02-9c55-410a-93a9-489c6f789798\"}";

        Mockito.when(jsonParser.parse(filePath, NotificationDetails.class)).thenReturn(notificationDetails);
        Mockito.when(jsonParser.serialize(notificationDetails.getNotificationContent())).thenReturn(notificationContent);
        Mockito.when(appValidator.validateUrlForValidUrlPatten(webhookUrl)).thenReturn(true);
        Mockito.when(httpClientWrapper.sendRequest(any(HttpRequest.class))).thenThrow(new IOException("Connection error"));

        Exception exception = Assertions.assertThrows(Exception.class, () -> processor.sendNotification(filePath));

        Mockito.verify(jsonParser).parse(filePath, NotificationDetails.class);
        Mockito.verify(appValidator).validateUrlForValidUrlPatten(webhookUrl);
        Mockito.verify(httpClientWrapper).sendRequest(any(HttpRequest.class));

        Assertions.assertEquals("Connection error", exception.getMessage());
    }

    @DisplayName("Should handle null response body")
    @Test
    void shouldHandleNullResponseBody() throws Exception {
        String filePath = "src/test/resources/valid_notification.json";
        String webhookUrl = "https://webhook.site/3b8c180a-dcac-4700-afee-9ebde4abbfcb";
        NotificationDetails notificationDetails = new NotificationDetails();
        notificationDetails.setNotificationUrl(webhookUrl);
        String notificationContent =  "{\"reportUID\":\"20fb8e02-9c55-410a-93a9-489c6f1d7598\",\"studyInstanceUID\":\"9998e02-9c55-410a-93a9-489c6f789798\"}";

        Mockito.when(jsonParser.parse(filePath, NotificationDetails.class)).thenReturn(notificationDetails);
        Mockito.when(jsonParser.serialize(notificationDetails.getNotificationContent())).thenReturn(notificationContent);
        Mockito.when(appValidator.validateUrlForValidUrlPatten(webhookUrl)).thenReturn(true);
        Mockito.when(httpResponse.body()).thenReturn(null);
        Mockito.when(httpResponse.statusCode()).thenReturn(HttpStatus.SC_OK);
        Mockito.when(httpClientWrapper.sendRequest(any(HttpRequest.class))).thenReturn(httpResponse);

        NotificationResponse response = processor.sendNotification(filePath);

        Mockito.verify(jsonParser).parse(filePath, NotificationDetails.class);
        Mockito.verify(appValidator).validateUrlForValidUrlPatten(webhookUrl);
        Mockito.verify(httpClientWrapper).sendRequest(any(HttpRequest.class));

        Assertions.assertEquals(webhookUrl, response.getNotificationUrl());
        Assertions.assertEquals(notificationContent, response.getContentSent());
        Assertions.assertEquals("", response.getResponseReceived());
        Assertions.assertEquals(HttpStatus.SC_OK, response.getResponseCode());
        Assertions.assertTrue(response.getResponseTime() >= 0);
    }
}
