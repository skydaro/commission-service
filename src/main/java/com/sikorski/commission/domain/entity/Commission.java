package com.sikorski.commission.domain.entity;

import lombok.*;

import javax.persistence.*;
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

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @ToString.Exclude
    @JoinColumn(name = "transaction_id")
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
