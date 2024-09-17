package com.motlient.notification.processor;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WebhookClientWireMockTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance().options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();

    private final HttpClientWrapper httpClient = new WebhookHttpClient();

    @DisplayName("Test for success response")
    @Test
    void testSuccessfulRequest() throws IOException, InterruptedException {

        String responseBody = "{ \"reportUID\": \"20fb8e02-9c55-410a-93a9-489c6f1d7598\", \"studyInstanceUID\": \"9998e02-9c55-410a-93a9-489c6f789798\" }";
        wireMock.stubFor(get(urlEqualTo("/3b8c180a-dcac-4700-afee-9ebde4abbfcb"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withBody(responseBody)));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + wireMock.getPort() + "/3b8c180a-dcac-4700-afee-9ebde4abbfcb"))
                .build();
        HttpResponse<String> response = httpClient.sendRequest(request);

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        Assertions.assertEquals(responseBody, response.body());
    }

    @DisplayName("Test for 404 response")
    @Test
    void testNotFoundRequest() throws IOException, InterruptedException {
        wireMock.stubFor(get(urlEqualTo("/3b8c180a-dcac-4700-afee-9ebde4abbfcb"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_NOT_FOUND)
                        .withBody("Not Found")));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + wireMock.getPort() + "/3b8c180a-dcac-4700-afee-9ebde4abbfcb"))
                .build();
        HttpResponse<String> response = httpClient.sendRequest(request);

        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode());
        Assertions.assertEquals("Not Found", response.body());
    }

    @DisplayName("Tst for 500 response")
    @Test
    void testServerError() throws IOException, InterruptedException {
        wireMock.stubFor(get(urlEqualTo("/3b8c180a-dcac-4700-afee-9ebde4abbfcb"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                        .withBody("Internal Server Error")));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + wireMock.getPort() + "/3b8c180a-dcac-4700-afee-9ebde4abbfcb"))
                .build();
        HttpResponse<String> response = httpClient.sendRequest(request);

        Assertions.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode());
        Assertions.assertEquals("Internal Server Error", response.body());
    }
}
