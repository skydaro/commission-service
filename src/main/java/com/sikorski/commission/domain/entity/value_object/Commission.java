package com.sikorski.commission.domain.entity.value_object;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.math.BigDecimal;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Commission {
    @AttributeOverrides(value = {
            @AttributeOverride(name = "amount", column = @Column(name = "commission_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "commission_currency"))
    })
    @Embedded
    private Money money;
    private String rule;

    public BigDecimal getAmount() {
        return money.getAmount();
    }

    public String getCurrencyCode() {
        return money.getCurrency().getCurrencyCode();
    }

    public String getInfoLog() {
        return "Commission calculated by " + rule + ": " + getAmount();
    }
}
