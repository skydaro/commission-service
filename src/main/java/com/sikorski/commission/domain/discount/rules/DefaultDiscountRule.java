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
public class DefaultDiscountRule extends Discount {

    private final DefaultDiscountRuleSettings settings;

    @Override
    public boolean matches(Client client) {
        return true;
    }

    @Override
    public Commission calculate(Client client) {
        var currentTransaction = client.getCurrentTransaction();
        var money =   currentTransaction.getMoney().multiply(settings.price);
        return new Commission(money.getAmount().max(settings.minAmount),
                this.getName(),
                currentTransaction);
    }

    @Data
    @Jacksonized
    @Builder
    public static class DefaultDiscountRuleSettings {
        private BigDecimal minAmount;
        private BigDecimal price;
    }
}