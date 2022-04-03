package com.into.space.assessment.exchangerate;

import com.into.space.assessment.exception.ExchangeRatesNotRespondException;
import com.into.space.assessment.transfer.domain.CurrencyExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stubbing EUR,USD,GBP pairs with 1,5sec sleep to imitate synchronous blocked call
 */
@Component
@Slf4j
public class ExchangeRatesWebClientStub implements ExchangeRatesProvider {

    //As of 01.04
    Map<String, CurrencyExchangeRate> exchangeRatesMap = Stream.of(
            new SimpleImmutableEntry<>("USD/EUR", new CurrencyExchangeRate("USD", "EUR", BigDecimal.valueOf(0.90).setScale(2, RoundingMode.HALF_UP))),
            new SimpleImmutableEntry<>("USD/GBP", new CurrencyExchangeRate("USD", "GBP", BigDecimal.valueOf(0.76).setScale(2, RoundingMode.HALF_UP))),
            new SimpleImmutableEntry<>("EUR/USD", new CurrencyExchangeRate("EUR", "USD", BigDecimal.valueOf(1.12).setScale(2, RoundingMode.HALF_UP))),
            new SimpleImmutableEntry<>("EUR/GBP", new CurrencyExchangeRate("EUR", "GBP", BigDecimal.valueOf(0.85).setScale(2, RoundingMode.HALF_UP))),
            new SimpleImmutableEntry<>("GBP/EUR", new CurrencyExchangeRate("GBP", "EUR", BigDecimal.valueOf(1.18).setScale(2, RoundingMode.HALF_UP))),
            new SimpleImmutableEntry<>("GBP/USD", new CurrencyExchangeRate("GBP", "USD", BigDecimal.valueOf(1.31).setScale(2, RoundingMode.HALF_UP)))
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    @Override
    public CurrencyExchangeRate getExchangeRateForPair(String sourceCurrency, String targetCurrency) throws ExchangeRatesNotRespondException {
        if (sourceCurrency.equals(targetCurrency)) {
            log.info("Source currency matches target currency, providing flat rate");
            return CurrencyExchangeRate.flat(sourceCurrency);
        }
        log.info("Fetching currency exchange rate for pair {}-{}", sourceCurrency, targetCurrency);
        try {
            //imitating synchronous call
            Thread.sleep(1500);
            return exchangeRatesMap.get(sourceCurrency + "/" + targetCurrency);
        } catch (Exception exception) {
            throw new ExchangeRatesNotRespondException("Exchange rates provider didn't respond");
        }
    }
}
