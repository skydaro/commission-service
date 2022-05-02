package com.sikorski.commission.domain.discount.rules;

import com.sikorski.commission.domain.discount.Discount;
import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.Commission;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Jacksonized
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HighTurnoverDiscountRule extends Discount {

    private final HighTurnoverDiscountRuleSettings settings;

    @Override
    public boolean matches(Client client) {
        var transactions = client.getTransactions();
        return transactions.getSumOfTransactions()
                .compareTo(settings.limit) >= 0;
    }

    @Override
    public Commission calculate(Client client) {
        var transactions = client.getTransactions();
        return new Commission(settings.amount,
                this.getName(),
                transactions.getCurrentTransaction());
    }

    @Data
    @Jacksonized
    @Builder
    public static class HighTurnoverDiscountRuleSettings {
        private BigDecimal limit;
        private BigDecimal amount;
    }
}