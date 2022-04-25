package com.sikorski.commission.domain.fx;

import java.time.LocalDate;

public class ExchangeRateNotFound extends Throwable {

    public ExchangeRateNotFound(String currency, LocalDate localDate) {
        super(String.format("Exchange rate for currency code %s not found as of %s", currency, localDate.toString()));
    }
}