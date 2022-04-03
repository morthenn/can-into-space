package com.into.space.assessment.account.service;

import com.into.space.assessment.account.domain.Account;
import com.into.space.assessment.exchangerate.ExchangeRatesProvider;
import com.into.space.assessment.exchangerate.ExchangeRatesWebClientStub;
import com.into.space.assessment.transfer.storage.business.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

//One and only integration test class in whole repo
@SpringBootTest
@Transactional
class AccountServiceITTest {

    private static final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(1000.00).setScale(2);
    //positive scenario
    private static final BigDecimal BIG_DECIMAL_ZERO = BigDecimal.valueOf(0.00).setScale(2);
    private static final BigDecimal BIG_DECIMAL_1120 = BigDecimal.valueOf(2120.00).setScale(2);
    //negative scenario
    private static final BigDecimal BIG_DECIMAL_5000 = BigDecimal.valueOf(5000.00).setScale(2);
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ExchangeRatesProvider exchangeRatesProviderStub = new ExchangeRatesWebClientStub();
    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @Test
    void shouldTransferMoneyFromSourceToTarget() throws InterruptedException {
        //given
        AccountService sut = new AccountService(accountRepository, exchangeRatesProviderStub, applicationEventPublisher);

        accountRepository.save(new Account(111111L, 1L, "EUR", INITIAL_AMOUNT, true));
        accountRepository.save(new Account(333333L, 3L, "USD", INITIAL_AMOUNT, true));

        //when
        sut.transfer("123", 111111L, 333333L, INITIAL_AMOUNT);

        //then
        Account senderAcc = accountRepository.getAccountById(111111L);
        Account receiverAcc = accountRepository.getAccountById(333333L);

        assertThat(senderAcc.getBalance()).isEqualTo(BIG_DECIMAL_ZERO);
        assertThat(receiverAcc.getBalance()).isEqualTo(BIG_DECIMAL_1120);
    }

    @Test
    void shouldNotChangeAnythingInCaseOfException() throws InterruptedException {
        //given
        AccountService sut = new AccountService(accountRepository, exchangeRatesProviderStub, applicationEventPublisher);

        Account okAccount = new Account(111111L, 1L, "EUR", INITIAL_AMOUNT, true);
        Account failingAccount = new Account(333333L, 3L, "USD", INITIAL_AMOUNT, false);

        accountRepository.save(okAccount);
        accountRepository.save(failingAccount);

        //when

        try {
            sut.transfer("123", 111111L, 333333L, BIG_DECIMAL_5000);
        } catch (Exception ignored) {
        }

        //then
        Account senderAcc = accountRepository.getAccountById(111111L);
        Account receiverAcc = accountRepository.getAccountById(333333L);

        assertThat(senderAcc.getBalance()).isEqualTo(INITIAL_AMOUNT);
        assertThat(receiverAcc.getBalance()).isEqualTo(INITIAL_AMOUNT);
    }


}
