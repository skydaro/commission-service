package com.sikorski.commission.domain.discount;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sikorski.commission.domain.discount.rules.ClientWithDiscountRule;
import com.sikorski.commission.domain.discount.rules.DefaultDiscountRule;
import com.sikorski.commission.domain.discount.rules.HighTurnoverDiscountRule;
import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.Commission;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientWithDiscountRule.class, name = "PLAIN"),
        @JsonSubTypes.Type(value = DefaultDiscountRule.class, name = "WITH_MIN"),
        @JsonSubTypes.Type(value = HighTurnoverDiscountRule.class, name = "WITH_LIMIT")
})
public interface DiscountRule {
    boolean matches(Client client);
    Commission calculate(Client client);
    String getName();
}