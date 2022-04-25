package com.sikorski.commission.domain.fx;

import com.sikorski.commission.ContainerizedIntegrationTest;
import com.sikorski.commission.MockWebServer;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(MockWebServer.class)
class ExchangeRateRepositoryTest extends ContainerizedIntegrationTest {
    public static final String SAMPLE_RATE = "4.636079";
    @Autowired
    private MockWebServer webServer;

    @Value("${feign.client.fx-client.url}")
    String fxClientUrl;

    @Autowired
    ExchangeRateRepository repository;

    @SneakyThrows
    @Test
    void getRateShouldReturnCorrectRate() {
        // given
        var mockResponse = getMockJsonResponse(true, "PLN");

        webServer.createMockServer(HttpStatus.OK, mockResponse, "/2022-04-25", "symbols", "PLN");

        // when
        var response = repository.getRate("PLN", LocalDate.now());

        // then
        Assertions.assertThat(response).isEqualTo(SAMPLE_RATE);
    }

    @Test
    @SneakyThrows
    void getRateWhichNotExistShouldThrowException() {
        // given
        var mockResponse = getMockJsonResponse(true, "KZT");

        webServer.createMockServer(HttpStatus.OK, mockResponse, "/2022-04-25", "symbols", "PLN");

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> repository.getRate("PLN", LocalDate.now());

        // then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ExchangeRateNotFound.class);
    }

    @Test
    @SneakyThrows
    void getRateWithNoSuccessShouldThrowException() {
        // given

        var mockResponse = getMockJsonResponse(false, "PLN");

        webServer.createMockServer(HttpStatus.OK, mockResponse, "/2022-04-25", "symbols", "PLN");

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> repository.getRate("PLN", LocalDate.now());

        // then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(ExchangeRateNotFound.class);
    }

    private String getMockJsonResponse(boolean success, String currency) {
        return String.format("""
                {
                    "success": %s,
                    "historical": true,
                    "base": "PN",
                    "date": "2022-04-21",
                    "rates": {
                        "%s": "%s"
                    }
                }""", success, currency, SAMPLE_RATE);
    }
}

