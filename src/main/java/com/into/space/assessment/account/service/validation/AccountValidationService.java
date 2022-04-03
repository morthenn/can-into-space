package com.into.space.assessment.account.service.validation;

import com.into.space.assessment.account.domain.AccountValidationView;
import com.into.space.assessment.exception.AccountNotExistException;
import com.into.space.assessment.exception.InsufficientFundsException;
import com.into.space.assessment.transfer.storage.business.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountValidationService {

    private final AccountRepository accountRepository;

    //Here can be also KYC validation service, LegacyAccountService to see if account is still alive, etc. directly connected to Account Validation

    public void validateAccountForFundsTransfer(Long senderAccId, Long receiverAccId, BigDecimal amount) {
        AccountValidationView accountValidationView = accountRepository.getAccountBalanceById(senderAccId);

        if (Objects.isNull(accountValidationView)) {
            throw new AccountNotExistException(String.format("Account with id %s does not exist", senderAccId));
        }

        if (accountValidationView.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds to proceed with transaction");
        }

        if (!accountRepository.accountExists(receiverAccId)) {
            throw new AccountNotExistException(String.format("Account with id %s does not exist", receiverAccId));
        }
    }
}
