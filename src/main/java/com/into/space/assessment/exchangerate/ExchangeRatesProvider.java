package com.into.space.assessment.exchangerate;

import com.into.space.assessment.exception.ExchangeRatesNotRespondException;
import com.into.space.assessment.transfer.domain.CurrencyExchangeRate;


public interface ExchangeRatesProvider {

    CurrencyExchangeRate getExchangeRateForPair(String sourceCurrency, String targetCurrency) throws ExchangeRatesNotRespondException;
}
