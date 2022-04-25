package com.sikorski.commission;

import lombok.RequiredArgsConstructor;
import org.mockserver.client.MockServerClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@RequiredArgsConstructor
@Import(MockServerClientConfiguration.class)
@TestConfiguration
public class MockWebServer {

    private final MockServerClient serverClient;

    public void createMockServer(HttpStatus statusCode, String responseFilePath, String path, String name, String value) {
        serverClient.reset().when(request()
                        .withMethod("GET")
                        .withPath(path)
                        .withQueryStringParameter(name, value))
                .respond(response()
                        .withStatusCode(statusCode.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseFilePath));
    }
}
