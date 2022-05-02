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
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Jacksonized
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClientWithDiscountRule extends Discount {
    private final ClientWithDiscountRuleSettings settings;

    @Override
    public boolean matches(Client client) {
        return client.hasDiscount();
    }

    @Override
    public Commission calculate(Client client) {
        var transactions = client.getTransactions();
        return new Commission(settings.amount,
                this.getName(), transactions.getCurrentTransaction());
    }

    @Data
    @Jacksonized
    @Builder
    public static class ClientWithDiscountRuleSettings {
        private BigDecimal amount;
    }
}