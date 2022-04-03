package com.into.space.assessment.transfer.storage.business.adapter;

import com.into.space.assessment.account.domain.Account;
import com.into.space.assessment.account.domain.AccountValidationView;
import com.into.space.assessment.exception.AccountNotExistException;
import com.into.space.assessment.transfer.storage.business.AccountRepository;
import com.into.space.assessment.transfer.storage.business.h2.H2AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class H2AccountDatabaseAdapter implements AccountRepository {

    private final H2AccountRepository h2AccountRepository;

    @Override
    public Account save(Account account) {
        return h2AccountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long accountId) {
        return h2AccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotExistException(String.format("Account with accountId:%s does not exist", accountId)));
    }

    @Override
    public AccountValidationView getAccountBalanceById(Long accountId) {
        return h2AccountRepository.getAccountBalanceByAccountId(accountId);
    }

    public boolean accountExists(Long accountId) {
        return h2AccountRepository.exists(accountWithId(accountId));
    }

    private Example<Account> accountWithId(Long accountId) {
        return Example.of(new Account(accountId, null, null, null, true));
    }
}
