package com.sikorski.commission.infrastructure.fx;

import com.sikorski.commission.infrastructure.fx.dto.ExchangeRateResponse;
import feign.Param;
import feign.RequestLine;

import java.time.LocalDate;

public interface ExchangeRateClient {
    @RequestLine("GET /{date}?symbols={currency}")
    ExchangeRateResponse getRates(@Param("date") LocalDate date, @Param("currency") String currency);
}