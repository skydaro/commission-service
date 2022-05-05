package com.sikorski.commission.domain.discount.rules;

import com.sikorski.commission.domain.discount.DiscountRule;
import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.value_object.Commission;
import com.sikorski.commission.domain.entity.value_object.Money;
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
public class ClientWithDiscountRule extends DiscountRule {
    private final ClientWithDiscountRuleSettings settings;

    @Override
    public boolean matches(Client client) {
        return client.hasDiscount();
    }

    @Override
    public Commission calculate(Client client) {
        return new Commission(new Money(settings.amount),
                this.getName());
    }

    @Jacksonized
    @Setter
    public static class ClientWithDiscountRuleSettings {
        private BigDecimal amount;
    }
}