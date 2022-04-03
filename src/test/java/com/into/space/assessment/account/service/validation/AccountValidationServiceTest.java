package com.into.space.assessment.account.service.validation;

import com.into.space.assessment.account.domain.AccountValidationView;
import com.into.space.assessment.exception.AccountNotExistException;
import com.into.space.assessment.exception.InsufficientFundsException;
import com.into.space.assessment.transfer.storage.business.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountValidationServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountValidationService accountValidationService;


    @Test
    void shouldThrowExceptionWhenNotExistingSenderAccount() {

        //given
        String expectedMsg = "Account with id 123 does not exist";
        when(accountRepository.getAccountBalanceById(any())).thenReturn(null);

        //when
        Throwable exception = assertThrows(AccountNotExistException.class,
                () -> accountValidationService.validateAccountForFundsTransfer(123L, 321L, BigDecimal.TEN));

        //then
        Assertions.assertThat(exception).isInstanceOf(AccountNotExistException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo(expectedMsg);
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFundsOnSenderAccount() {

        //given
        String expectedMsg = "Insufficient funds to proceed with transaction";
        AccountValidationViewImpl accountValidationViewWithZeroBalance = new AccountValidationViewImpl(123L, BigDecimal.ZERO);
        when(accountRepository.getAccountBalanceById(any())).thenReturn(accountValidationViewWithZeroBalance);

        //when
        Throwable exception = assertThrows(InsufficientFundsException.class,
                () -> accountValidationService.validateAccountForFundsTransfer(123L, 321L, BigDecimal.TEN));

        //then
        Assertions.assertThat(exception).isInstanceOf(InsufficientFundsException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo(expectedMsg);
    }

    @Test
    void shouldThrowExceptionWhenNotExistingReceiverAccount() {

        //given
        String expectedMsg = "Account with id 321 does not exist";
        AccountValidationViewImpl validSenderAccountView = new AccountValidationViewImpl(123L, BigDecimal.valueOf(10000));
        when(accountRepository.getAccountBalanceById(123L)).thenReturn(validSenderAccountView);
        when(accountRepository.accountExists(321L)).thenReturn(false);

        //when
        Throwable exception = assertThrows(AccountNotExistException.class,
                () -> accountValidationService.validateAccountForFundsTransfer(123L, 321L, BigDecimal.TEN));

        Assertions.assertThat(exception).isInstanceOf(AccountNotExistException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo(expectedMsg);
    }


    private static class AccountValidationViewImpl implements AccountValidationView {

        private final Long accountId;

        private final BigDecimal balance;

        public AccountValidationViewImpl(Long accountId, BigDecimal balance) {
            this.accountId = accountId;
            this.balance = balance;
        }

        @Override
        public Long getAccountId() {
            return this.accountId;
        }

        @Override
        public BigDecimal getBalance() {
            return this.balance;
        }
    }
}