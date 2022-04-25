package com.sikorski.commission.domain.discount;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public abstract class Discount implements DiscountRule {
    protected String name;
}
