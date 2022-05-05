package com.sikorski.commission.domain.discount;

import com.sikorski.commission.ContainerizedIntegrationTest;
import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.value_object.Money;
import com.sikorski.commission.domain.entity.Transaction;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static java.math.BigDecimal.valueOf;

class DiscountEngineIntegrationTest extends ContainerizedIntegrationTest {
    private static final LocalDate SAMPLE_DATE = LocalDate.of(2022, 2, 1);
    private static final int SAMPLE_CLIENT_ID = 1;

    @Autowired
    DiscountEngine discountEngine;

    @Test
    void process_checkIfDefaultDiscountRuleWasUsed() {
        // given
        var client = createSampleClient(false);
        var transaction = createSampleTransaction("150.00", client, true);
        client.calculateCommission(transaction, discountEngine);

        // when
        var result = discountEngine.process(client);

        // then
        Assertions.assertThat(result.getAmount()).isEqualByComparingTo(valueOf(0.75));
    }

    @Test
    void process_checkIfClientWithDiscountRuleWasUsed() {
        // given
        var client = createSampleClient(true);
        var transaction = createSampleTransaction("500", client, true);

        // when
        var result = client.calculateCommission(transaction, discountEngine);

        // then
        Assertions.assertThat(result.getAmount()).isEqualByComparingTo(valueOf(0.05));
    }

    @Test
    void process_checkIfHighTurnoverDiscountRuleWasUsed() {
        // given
        var client = createSampleClient(true);
        client.getTransactions().addTransaction(createSampleTransaction("1500", null, false));

        // when
        var result = client.calculateCommission(createSampleTransaction("7000", client, true), discountEngine);

        // then
        Assertions.assertThat(result.getAmount()).isEqualByComparingTo(valueOf(0.03));
    }


    @NotNull
    private Transaction createSampleTransaction(String amount, Client client, boolean isCurrent) {
        return new Transaction(new Money(amount), SAMPLE_DATE, client, ZonedDateTime.now(ZoneOffset.UTC), isCurrent);
    }

    @NotNull
    private Client createSampleClient(boolean hasDiscount) {
        return new Client(SAMPLE_CLIENT_ID, hasDiscount);
    }
}