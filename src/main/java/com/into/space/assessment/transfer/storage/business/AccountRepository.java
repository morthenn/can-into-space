package com.into.space.assessment.transfer.storage.business;

import com.into.space.assessment.account.domain.Account;
import com.into.space.assessment.account.domain.AccountValidationView;

public interface AccountRepository {

    Account save(Account account);

    Account getAccountById(Long accountId);

    AccountValidationView getAccountBalanceById(Long accountId);

    boolean accountExists(Long accountId);

}
