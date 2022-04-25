package com.sikorski.commission;

import org.mockserver.client.MockServerClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockServerClientConfiguration extends ContainerizedIntegrationTest {
    @Bean
    public MockServerClient getMockServerClient() {
        return new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort());
    }
}
