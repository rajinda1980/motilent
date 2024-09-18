package com.motlient.notification.processor;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * A simple implementation of the HttpClientWrapper interface that uses Java's HttpClient to send HTTP requests and returns the response as a
 * string. This class provides a basic way to interact with HTTP endpoints by leveraging Java's built-in HTTP client functionality.
 */
public class WebhookHttpClient implements HttpClientWrapper {

    @Override
    public HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
