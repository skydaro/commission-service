package com.sikorski.commission.domain.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "commissions")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Commission extends BaseEntity {
    @Embedded
    private Money money;
    private String rule;

    public Commission(BigDecimal amount, String rule, Transaction transaction) {
        this.money = new Money(amount);
        this.rule = rule;
        this.transaction = transaction;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Transaction transaction;

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
