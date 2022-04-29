package com.sikorski.commission.domain.fx;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ExchangeRateNotFound extends RuntimeException {

    public ExchangeRateNotFound(String currency, LocalDate localDate) {
        super(String.format("Exchange rate for currency code %s not found as of %s", currency, localDate.toString()));
    }
}