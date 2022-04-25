package com.sikorski.commission.infrastructure.fx;

import com.sikorski.commission.domain.fx.ExchangeRateRepository;
import com.sikorski.commission.domain.fx.ExchangeRateNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
    private final ExchangeRateClient client;

    public String getRate(String currency, LocalDate date) throws ExchangeRateNotFound {
        var response = client.getRates(date, currency);
        if (response.isSuccess() && response.getRates().containsKey(currency)) {
            return response.getRates().get(currency);
        }
        throw new ExchangeRateNotFound(currency, date);
    }
}