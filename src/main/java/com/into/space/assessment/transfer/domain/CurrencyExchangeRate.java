package com.into.space.assessment.transfer.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyExchangeRate {

    String sourceCurrency;

    String targetCurrency;

    BigDecimal exchangeRate;

    public static CurrencyExchangeRate flat(String currency) {
        return new CurrencyExchangeRate(currency, currency, BigDecimal.ONE);
    }

    public BigDecimal getExchangeRate() {
        return this.exchangeRate.setScale(2, RoundingMode.HALF_UP);
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate.setScale(2, RoundingMode.HALF_UP);
    }
}
