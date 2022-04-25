package com.sikorski.commission.domain.entity;

import com.sikorski.commission.domain.discount.DiscountEngine;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Client extends BaseEntity {
    private Integer clientId;

    @Column(name = "has_discount")
    private boolean hasDiscount;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    @Version
    private Long version;

    public Client(Integer clientId) {
        this.clientId = clientId;
    }

    public boolean hasDiscount() {
        return this.hasDiscount;
    }

    public static Client create(Integer clientId) {
        return new Client(clientId);
    }

    public Commission commitTransaction(Transaction transaction, DiscountEngine engine) {
        addTransaction(transaction);
        return engine.process(this);
    }

    public Transaction getCurrentTransaction() {
        return this.getTransactions().stream()
                .filter(Transaction::isCurrent)
                .findFirst()
                .orElseThrow();
    }

    public BigDecimal getSumOfTransactions() {
        return this.getTransactions().stream()
                .filter(transaction -> transaction.hasEqualMonthAndYear(getCurrentTransaction()))
                .filter(Transaction::isArchived)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
