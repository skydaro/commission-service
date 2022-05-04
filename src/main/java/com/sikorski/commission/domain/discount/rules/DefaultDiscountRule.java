package com.sikorski.commission.domain.discount.rules;

import com.sikorski.commission.domain.discount.DiscountRule;
import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.Commission;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DefaultDiscountRule extends DiscountRule {

    private final DefaultDiscountRuleSettings settings;

    @Override
    public boolean matches(Client client) {
        return true;
    }

    @Override
    public Commission calculate(Client client) {
        var transactions = client.getTransactions();
        var currentTransaction = transactions.getCurrentTransaction();
        var money =   currentTransaction.getMoney().multiply(settings.price);
        return new Commission(money.getAmount().max(settings.minAmount),
                this.getName(),
                currentTransaction);
    }

    @Jacksonized
    @Setter
    public static class DefaultDiscountRuleSettings {
        private BigDecimal minAmount;
        private BigDecimal price;
    }
}