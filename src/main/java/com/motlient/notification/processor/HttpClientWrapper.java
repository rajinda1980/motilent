package com.motlient.notification.processor;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface HttpClientWrapper {
    HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException;
}
