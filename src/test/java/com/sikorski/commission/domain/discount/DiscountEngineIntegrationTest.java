package com.sikorski.commission.domain.discount;

import com.sikorski.commission.ContainerizedIntegrationTest;
import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.Money;
import com.sikorski.commission.domain.entity.Transaction;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

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
        client.commitTransaction(transaction, discountEngine);

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
        var result = client.commitTransaction(transaction, discountEngine);

        // then
        Assertions.assertThat(result.getAmount()).isEqualByComparingTo(valueOf(0.05));
    }

    @Test
    void process_checkIfHighTurnoverDiscountRuleWasUsed() {
        // given
        Set<Transaction> transactions = new HashSet<>();
        transactions.add(createSampleTransaction("1500", null, false));
        var client = createSampleClient(true, transactions);

        // when
        var result = client.commitTransaction(createSampleTransaction("7000", client, true), discountEngine);

        // then
        Assertions.assertThat(result.getAmount()).isEqualByComparingTo(valueOf(0.03));
    }


    @NotNull
    private Transaction createSampleTransaction(String amount, Client client, boolean isCurrent) {
        return new Transaction(new Money(amount), SAMPLE_DATE, client, ZonedDateTime.now(ZoneOffset.UTC), isCurrent);
    }

    @NotNull
    private Client createSampleClient(boolean hasDiscount) {
        return new Client(SAMPLE_CLIENT_ID, hasDiscount, new HashSet<>(), 0L);
    }

    @NotNull
    private Client createSampleClient(boolean hasDiscount, Set<Transaction> transactions) {
        return new Client(SAMPLE_CLIENT_ID, hasDiscount, transactions, 0L);
    }
}