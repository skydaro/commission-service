package com.sikorski.commission.api.dto;

import com.sikorski.commission.domain.entity.Commission;
import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Builder
public class CommissionResponse {
    private final String amount;
    private final String currency;

    public CommissionResponse(Commission commission) {
        this.amount = commission.getAmount().toString();
        this.currency = commission.getMoney().getCurrency().getCurrencyCode();
    }
}