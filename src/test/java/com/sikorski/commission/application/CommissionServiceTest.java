package com.sikorski.commission.application;

import com.sikorski.commission.ContainerizedIntegrationTest;
import com.sikorski.commission.api.dto.CommissionResponse;
import com.sikorski.commission.api.dto.TransactionRequest;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

class CommissionServiceTest extends ContainerizedIntegrationTest {

    @Autowired
    CommissionService commissionService;

    @SneakyThrows
    @ParameterizedTest
    @CsvFileSource(resources = "/test.csv", numLinesToSkip = 1)
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
}