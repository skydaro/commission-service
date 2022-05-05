package com.sikorski.commission.domain.entity;

import com.sikorski.commission.domain.entity.value_object.Commission;
import com.sikorski.commission.domain.entity.value_object.Money;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Transaction extends BaseEntity {
    @Embedded
    private Money money;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private ZonedDateTime dateCreated;

    @Transient
    private boolean isCurrent;

    @Embedded
    private Commission commission = new Commission();

    public Transaction(Money money, LocalDate date, Client client, ZonedDateTime dateCreated, boolean isCurrent) {
        this.money = money;
        this.date = date;
        this.client = client;
        this.dateCreated = dateCreated;
        this.isCurrent = isCurrent;
    }

    public static Transaction create(Money money, LocalDate date, Client client) {
        return new Transaction(money, date, client, ZonedDateTime.now(ZoneOffset.UTC), true);
    }

    public BigDecimal getAmount() {
        return money.getAmount();
    }

    boolean hasEqualMonthAndYear(Transaction transaction) {
        return date.getMonth() == transaction.getDate().getMonth()
                && date.getYear() == transaction.getDate().getYear();
    }

    boolean isArchived() {
        return !isCurrent;
    }

    void addCommission(Commission commission) {
        this.commission = commission;
    }
}
