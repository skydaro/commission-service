package com.sikorski.commission.domain.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExchangeRateRepository {
    BigDecimal getRate(String currency, LocalDate date);
}