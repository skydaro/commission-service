package com.sikorski.commission.application;

import com.sikorski.commission.ContainerizedIntegrationTest;
import com.sikorski.commission.api.controller.dto.CommissionResponse;
import com.sikorski.commission.api.controller.dto.TransactionRequest;
import com.sikorski.commission.domain.repository.ClientRepository;
import com.sikorski.commission.domain.exception.InvalidDate;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class CommissionServiceTest extends ContainerizedIntegrationTest {

    public static final int CLIENT_ID = 1;
    public static final String EUR = "EUR";
    public static final String SAMPLE_AMOUNT = "100.00";
    public static final LocalDate SAMPLE_DATE = LocalDate.of(2022, 4, 1);

    @Autowired
    CommissionService commissionService;

    @MockBean
    TimeService timeService;

    @Autowired
    ClientRepository clientRepository;

    @BeforeEach
    void setup() {
        when(timeService.getNow()).thenAnswer(invocation -> LocalDate.now());
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvFileSource(resources = "/test.csv", numLinesToSkip = CLIENT_ID)
    void getCommission(Integer clientId, LocalDate date, String amount, String currency, String commissionAmount, String commissionCurrency) {
        // given
        var request = TransactionRequest.builder()
                .clientId(clientId)
                .amount(amount)
                .date(date)
                .currency(currency)
                .build();

        // when
        var response = commissionService.getCommission(request);

        // then
        Assertions.assertThat(response)
                .isEqualTo(CommissionResponse.builder()
                        .amount(commissionAmount)
                        .currency(commissionCurrency)
                        .build());
    }

    @SneakyThrows
    @Test
    void checkIfClientIsSaved() {
        // given
        var request = sampleRequest();

        // when
        Assertions.assertThat(clientRepository.findClientByClientId(CLIENT_ID)).isEmpty();
        commissionService.getCommission(request);

        // then
        Assertions.assertThat(clientRepository.findClientByClientId(CLIENT_ID)).isNotEmpty();
    }

    @Test
    void futureRequestDateShouldThrowException() {
        // given
        var request = sampleRequest();

        // when
        when(timeService.getNow()).thenAnswer(invocation -> SAMPLE_DATE.minusDays(1));
        ThrowableAssert.ThrowingCallable throwingCallable = () -> commissionService.getCommission(request);

        // then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(InvalidDate.class);
    }

    @Test
    void pastRequestDateShouldNotThrowException() {
        // given
        var request = sampleRequest();

        // when
        when(timeService.getNow()).thenAnswer(invocation -> SAMPLE_DATE.plusDays(1));
        ThrowableAssert.ThrowingCallable throwingCallable = () -> commissionService.getCommission(request);

        // then
        assertThatNoException().isThrownBy(throwingCallable);
    }

    @Test
    void sameDayRequestDateShouldNotThrowException() {
        // given
        var request = sampleRequest();

        // when
        when(timeService.getNow()).thenAnswer(invocation -> SAMPLE_DATE);
        ThrowableAssert.ThrowingCallable throwingCallable = () -> commissionService.getCommission(request);

        // then
        assertThatNoException().isThrownBy(throwingCallable);
    }

    private TransactionRequest sampleRequest() {
        return TransactionRequest.builder()
                .clientId(CLIENT_ID)
                .amount(SAMPLE_AMOUNT)
                .date(SAMPLE_DATE)
                .currency(EUR)
                .build();
    }
}