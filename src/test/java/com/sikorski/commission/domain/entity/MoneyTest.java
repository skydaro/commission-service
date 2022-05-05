package com.sikorski.commission.domain.entity;

import com.sikorski.commission.domain.entity.value_object.Money;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

class MoneyTest {

    @ParameterizedTest(name = "{0}/{1}={2}")
    @MethodSource("convertingWithRatesValues")
    void divide(String amount, BigDecimal rate, BigDecimal output) {
        var money = new Money(amount);
        Assertions.assertThat(money.divide(rate).getAmount()).isEqualByComparingTo(output);
    }

    private static Stream<Arguments> convertingWithRatesValues() {
        return Stream.of(
                Arguments.of("100.00", BigDecimal.valueOf(4.636079), BigDecimal.valueOf(21.57)),
                Arguments.of("99.97", BigDecimal.valueOf(4.636079), BigDecimal.valueOf(21.56)),
                Arguments.of("10.01", BigDecimal.valueOf(482.657706), BigDecimal.valueOf(0.02))
                );
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("convertingStringToBigDecimalValues")
    void stringToBigDecimal(String input, BigDecimal output) {
        Assertions.assertThat(new Money(input).getAmount()).isEqualByComparingTo(output);
    }

    private static Stream<Arguments> convertingStringToBigDecimalValues() {
        return Stream.of(
                Arguments.of("100.00", BigDecimal.valueOf(100)),
                Arguments.of("99.978", BigDecimal.valueOf(99.98)),
                Arguments.of("00.01",  BigDecimal.valueOf(0.01))
        );
    }

    @ParameterizedTest(name = "{0}*{1}={2}")
    @MethodSource("multiplyValues")
    void multiply(String input, BigDecimal multiplier, BigDecimal output) {
        Assertions.assertThat(new Money(input).multiply(multiplier).getAmount()).isEqualByComparingTo(output);
    }

    private static Stream<Arguments> multiplyValues() {
        return Stream.of(
                Arguments.of("100.00", BigDecimal.valueOf(0.003), BigDecimal.valueOf(0.3)),
                Arguments.of("499.00", BigDecimal.valueOf(0.005), BigDecimal.valueOf(2.5)),
                Arguments.of("99.99", BigDecimal.valueOf(0.009), BigDecimal.valueOf(0.9))
        );
    }
}