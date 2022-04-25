package com.sikorski.commission.infrastructure.fx;

import com.sikorski.commission.domain.fx.ExchangeRateNotFound;
import com.sikorski.commission.domain.fx.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
    private final ExchangeRateClient client;

    public BigDecimal getRate(String currency, LocalDate date) {
        var response = client.getRates(date, currency);
        if (response.isSuccess() && response.getRates().containsKey(currency)) {
            return response.getRates().get(currency);
        }

        throw new ExchangeRateNotFound(currency, date);
    }
}