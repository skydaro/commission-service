package com.sikorski.commission.domain.entity;

import com.sikorski.commission.domain.discount.DiscountEngine;
import com.sikorski.commission.domain.entity.value_object.Commission;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "clients")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Client extends BaseEntity {
    @Column(unique = true)
    private Integer clientId;

    @Column(name = "has_discount")
    private boolean hasDiscount;

    @Embedded
    private Transactions transactions = new Transactions();

    public Client(Integer clientId) {
        this.clientId = clientId;
    }

    public Client(Integer clientId, boolean hasDiscount) {
        this.clientId = clientId;
        this.hasDiscount = hasDiscount;
    }

    public static Client create(Integer clientId) {
        return new Client(clientId);
    }

    public boolean hasDiscount() {
        return this.hasDiscount;
    }

    public Commission calculateCommission(Transaction transaction, DiscountEngine discountEngine) {
        transactions.addTransaction(transaction);
        var commission = discountEngine.process(this);
        transaction.addCommission(commission);
        return commission;
    }
}
