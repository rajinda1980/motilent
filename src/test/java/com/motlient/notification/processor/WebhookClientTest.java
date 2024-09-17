package com.motlient.notification.processor;

import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebhookClientTest {

    @DisplayName("Test for success 200 response")
    @Test
    void testSuccessfulRequest() throws IOException, InterruptedException {
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);

        Mockito.when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
        Mockito.when(mockResponse.statusCode()).thenReturn(HttpStatus.SC_OK);
        Mockito.when(mockResponse.body()).thenReturn("Success");

        HttpClientWrapper httpClient = new WebhookHttpClient() {
            @Override
            public HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
                return mockHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost/webhook")).build();
        HttpResponse<String> response = httpClient.sendRequest(request);

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        Assertions.assertEquals("Success", response.body());
    }

    @DisplayName("Test for 404 error response")
    @Test
    void testNotFoundRequest() throws IOException, InterruptedException {
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);

        Mockito.when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
        Mockito.when(mockResponse.statusCode()).thenReturn(HttpStatus.SC_NOT_FOUND);
        Mockito.when(mockResponse.body()).thenReturn("Not Found");

        HttpClientWrapper httpClient = new WebhookHttpClient() {
            @Override
            public HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
                return mockHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost/webhook")).build();
        HttpResponse<String> response = httpClient.sendRequest(request);

        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode());
        Assertions.assertEquals("Not Found", response.body());
    }

    @DisplayName("Test for 500 internal server error response")
    @Test
    void testServerError() throws IOException, InterruptedException {
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);

        Mockito.when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
        Mockito.when(mockResponse.statusCode()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        Mockito.when(mockResponse.body()).thenReturn("Internal Server Error");

        HttpClientWrapper httpClient = new WebhookHttpClient() {
            @Override
            public HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
                return mockHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost/webhook")).build();
        HttpResponse<String> response = httpClient.sendRequest(request);

        Assertions.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode());
        Assertions.assertEquals("Internal Server Error", response.body());
    }
}
