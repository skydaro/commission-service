package com.sikorski.commission.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Builder
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

    public BigDecimal getAmount() {
        return money.getAmount();
    }

    public static Transaction create(Money money, LocalDate date, Client client) {
        return new Transaction(money, date, client, ZonedDateTime.now(ZoneOffset.UTC), true);
    }

    public boolean hasEqualMonthAndYear(Transaction transaction) {
        return date.getMonth() == transaction.getDate().getMonth()
                && date.getYear() == transaction.getDate().getYear();
    }

    public boolean isArchived() {
        return !isCurrent;
    }
}
