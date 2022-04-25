package com.sikorski.commission.infrastructure.fx.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public final class ExchangeRateResponse {
    private boolean success;
    private HashMap<String, String> rates;
}