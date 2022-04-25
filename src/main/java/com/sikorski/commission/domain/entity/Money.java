package com.sikorski.commission.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;

    private BigDecimal amount;
    private Currency currency;

    public Money divide(String rate) {
        return new Money(amount.divide(convert(rate), currency.getDefaultFractionDigits(), DEFAULT_ROUNDING));
    }

    public Money multiply(BigDecimal amount) {
        return new Money(this.getAmount()
                .multiply(amount, new MathContext(currency.getDefaultFractionDigits(), DEFAULT_ROUNDING)));
    }

    public Money(BigDecimal amount) {
        this(amount, EUR, DEFAULT_ROUNDING);
    }

    public Money(String amount) {
        this.currency = EUR;
        this.amount = BigDecimal.valueOf(Double.parseDouble(amount)).setScale(currency.getDefaultFractionDigits(), DEFAULT_ROUNDING);
    }

    public Money(BigDecimal amount, Currency currency, RoundingMode rounding) {
        this.currency = currency;
        this.amount = amount.setScale(currency.getDefaultFractionDigits(), rounding);
    }

    public boolean isDefaultCurrency(String currency) {
        return this.currency.getCurrencyCode().equals(currency);
    }

    private static BigDecimal convert(String amount) {
        return BigDecimal.valueOf(Double.parseDouble(amount));
    }

    @Override
    public String toString() {
        return getCurrency().getSymbol() + " " + getAmount();
    }
}