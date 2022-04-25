package com.sikorski.commission.domain.fx;

import java.time.LocalDate;

public interface ExchangeRateRepository {
    String getRate(String currency, LocalDate date) throws ExchangeRateNotFound;
}