package com.sikorski.commission.domain.entity;

import com.sikorski.commission.domain.discount.DiscountEngine;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Client extends BaseEntity {
    @Column(unique = true)
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

    public Commission calculateCommission(Transaction transaction, DiscountEngine discountEngine) {
        this.transactions.add(transaction);
        var commission = discountEngine.process(this);
        transaction.addCommission(commission);
        return commission;
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
}
