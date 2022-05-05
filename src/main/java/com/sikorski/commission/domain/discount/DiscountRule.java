package com.sikorski.commission.domain.discount;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sikorski.commission.domain.discount.rules.ClientWithDiscountRule;
import com.sikorski.commission.domain.discount.rules.DefaultDiscountRule;
import com.sikorski.commission.domain.discount.rules.HighTurnoverDiscountRule;
import com.sikorski.commission.domain.entity.Client;
import com.sikorski.commission.domain.entity.value_object.Commission;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientWithDiscountRule.class, name = "PLAIN"),
        @JsonSubTypes.Type(value = DefaultDiscountRule.class, name = "WITH_MIN"),
        @JsonSubTypes.Type(value = HighTurnoverDiscountRule.class, name = "WITH_LIMIT")
})
public abstract class DiscountRule {
    public abstract boolean matches(Client client);
    public abstract Commission calculate(Client client);
    protected String name;
}
