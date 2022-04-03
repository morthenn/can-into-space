package com.into.space.assessment.account.domain;

import java.math.BigDecimal;

public interface AccountValidationView {

    Long getAccountId();

    BigDecimal getBalance();
}
