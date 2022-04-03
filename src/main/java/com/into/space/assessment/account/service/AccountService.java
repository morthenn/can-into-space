package com.into.space.assessment.account.service;

import com.into.space.assessment.account.domain.Account;
import com.into.space.assessment.exception.AccountNotActiveException;
import com.into.space.assessment.exception.ExchangeRatesNotRespondException;
import com.into.space.assessment.exception.FundsTransferException;
import com.into.space.assessment.exception.InsufficientFundsException;
import com.into.space.assessment.exchangerate.ExchangeRatesProvider;
import com.into.space.assessment.transfer.domain.CurrencyExchangeRate;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.service.async.event.TransactionUpdateEvent;
import com.into.space.assessment.transfer.storage.business.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.into.space.assessment.transfer.domain.enumeration.Status.PENDING;
import static com.into.space.assessment.transfer.domain.enumeration.Status.SUCCESS;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    private final ExchangeRatesProvider exchangeRatesProvider;

    private final ApplicationEventPublisher applicationEventPublisher;

    private void withdraw(Account account, BigDecimal amount) throws InterruptedException, AccountNotActiveException {
        BigDecimal newBalance = account.getBalance().subtract(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Insufficient funds on account");
        }

        //I think it could be checked here since lots of finance operations would use withdraw method IRL
        checkIfAccountIsActive(account);

        account.setBalance(newBalance);

        //imitating legacy integration
        Thread.sleep(500);
        log.info("Deducted {} {} from accountId={}", amount, account.getCurrency(), account.getAccountId());
    }

    private void deposit(Account account, BigDecimal amount) throws InterruptedException {
        BigDecimal newBalance = account.getBalance().add(amount);

        checkIfAccountIsActive(account);

        account.setBalance(newBalance);

        //imitating legacy integration
        Thread.sleep(500);
        log.info("Added {} {} to accountId={}", amount, account.getCurrency(), account.getAccountId());
    }

    @Transactional
    public void transfer(String transactionGuardId, Long sourceAccountId, Long targetAccountId, BigDecimal amount) throws FundsTransferException, ExchangeRatesNotRespondException, InterruptedException, AccountNotActiveException {
        Account senderAccount = accountRepository.getAccountById(sourceAccountId);
        Account receiverAccount = accountRepository.getAccountById(targetAccountId);

        CurrencyExchangeRate exchangeRate = exchangeRatesProvider.getExchangeRateForPair(senderAccount.getCurrency(), receiverAccount.getCurrency());

        log.info("Successfully fetched exchange rate = {}", exchangeRate);

        emitTransactionUpdateStatus(transactionGuardId, PENDING);

        BigDecimal calculatedAmount = exchangeRate.getExchangeRate().multiply(amount)
                .setScale(2, RoundingMode.HALF_UP);

        log.info("Calculated target amount using rate {} -$$$-> {} {} = {} {}", exchangeRate.getExchangeRate(),
                amount,
                senderAccount.getCurrency(),
                calculatedAmount,
                receiverAccount.getCurrency()
        );
        withdraw(senderAccount, amount);
        deposit(receiverAccount, calculatedAmount);
        log.info("Funds transfer successful");

        emitTransactionUpdateStatus(transactionGuardId, SUCCESS);
    }

    @SneakyThrows
    private void emitTransactionUpdateStatus(String transactionGuardId, Status status) {
        //because it looks awesome in logs :D
        Thread.sleep(1200);
        applicationEventPublisher.publishEvent(new TransactionUpdateEvent(this, transactionGuardId, status, null));
    }

    private boolean checkIfAccountIsActive(Account account) throws AccountNotActiveException {
        if (Boolean.FALSE.equals(account.getIsActive())) {
            throw new AccountNotActiveException(
                    String.format("Account: %s - is NOT active. Could not complete request", account.getAccountId()));
        }
        return true;
    }
}
