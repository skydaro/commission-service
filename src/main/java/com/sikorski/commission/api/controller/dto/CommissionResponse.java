package com.sikorski.commission.api.controller.dto;

import lombok.*;

@Value
@Builder
public class CommissionResponse {
    String amount;
    String currency;
}