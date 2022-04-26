package com.sikorski.commission.infrastructure.fx;

import com.sikorski.commission.domain.fx.ExchangeRateNotFound;
import com.sikorski.commission.domain.fx.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
    private final ExchangeRateClient client;

    public BigDecimal getRate(String currency, LocalDate date) {
        var response = client.getRates(date, currency);
        if (response.isSuccess() && response.getRates().containsKey(currency)) {
            log.info("Exchange Rates API response for {} {} received: {}", date, currency, response);
            return response.getRates().get(currency);
        }

        throw new ExchangeRateNotFound(currency, date);
    }
}