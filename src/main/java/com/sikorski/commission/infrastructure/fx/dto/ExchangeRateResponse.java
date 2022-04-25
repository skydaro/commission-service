package com.sikorski.commission.infrastructure.fx.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.util.HashMap;

@Value
public class ExchangeRateResponse {
    boolean success;
    HashMap<String, BigDecimal> rates;
}