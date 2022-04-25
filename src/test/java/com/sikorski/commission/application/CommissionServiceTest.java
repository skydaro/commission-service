package com.sikorski.commission.application;

import com.sikorski.commission.ContainerizedIntegrationTest;
import com.sikorski.commission.api.dto.CommissionResponse;
import com.sikorski.commission.api.dto.TransactionRequest;
import com.sikorski.commission.domain.dao.ClientRepository;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

class CommissionServiceTest extends ContainerizedIntegrationTest {

    @Autowired
    CommissionService commissionService;

    @Autowired
    ClientRepository clientRepository;

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

    @SneakyThrows
    @Test
    void checkIfClientIsSaved() {
        // given
        var clientId = 1;
        var request = TransactionRequest.builder()
                .clientId(clientId)
                .amount("100")
                .date(LocalDate.of(2022,4,1))
                .currency("EUR")
                .build();

        // when
        Assertions.assertThat(clientRepository.findClientByClientId(clientId)).isEmpty();
        commissionService.getCommission(request);

        // then
        Assertions.assertThat(clientRepository.findClientByClientId(clientId)).isNotEmpty();
    }
}