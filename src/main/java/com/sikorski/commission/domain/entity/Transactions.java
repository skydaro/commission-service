package com.sikorski.commission.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Embeddable
@EqualsAndHashCode
@ToString
public class Transactions {

    @Getter
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public Transaction getCurrentTransaction() {
        return this.transactions.stream()
                .filter(Transaction::isCurrent)
                .findFirst()
                .orElseThrow();
    }

    public BigDecimal getSumOfTransactions() {
        return this.transactions.stream()
                .filter(transaction -> transaction.hasEqualMonthAndYear(getCurrentTransaction()))
                .filter(Transaction::isArchived)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
