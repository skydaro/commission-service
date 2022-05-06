package com.sikorski.commission.infrastructure.fx;

import com.sikorski.commission.application.TimeService;
import com.sikorski.commission.domain.exception.ExchangeRateNotFound;
import com.sikorski.commission.domain.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
    private static final int CACHE_EXPIRATION_NUMBER_OF_DAYS = 30;
    private final ExchangeRateClient exchangeRateClient;
    private final RedissonClient redissonClient;
    private final TimeService timeService;

    public BigDecimal getRate(String currency, LocalDate date) {
        var map = getMap(date);
        var rate = map.computeIfAbsent(currency, c -> findRate(c, date));
        if (map.getExpireTime() == -1) {
            map.expire(getCacheDuration(date));
        }
        return rate;
    }

    private RMap<String, BigDecimal> getMap(LocalDate date) {
        return redissonClient.getMap(date.toString());
    }

    private BigDecimal findRate(String currency, LocalDate date) {
        var response = exchangeRateClient.getRates(date, currency);
        if (response.isSuccess() && response.getRates().containsKey(currency)) {
            log.info("Exchange Rates API response for {} {} received: {}", date, currency, response);
            return response.getRates().get(currency);
        }

        throw new ExchangeRateNotFound(currency, date);
    }

    private Duration getCacheDuration(LocalDate date) {
        return Duration.ofDays(CACHE_EXPIRATION_NUMBER_OF_DAYS - DAYS.between(date, timeService.getNow()));
    }
}