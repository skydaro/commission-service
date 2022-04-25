package com.sikorski.commission.domain.fx;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExchangeRateRepository {
    BigDecimal getRate(String currency, LocalDate date);
}