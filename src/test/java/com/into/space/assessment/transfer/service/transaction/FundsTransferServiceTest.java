package com.into.space.assessment.transfer.service.transaction;

import com.into.space.assessment.account.service.AccountService;
import com.into.space.assessment.exception.InsufficientFundsException;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.service.async.event.TransactionUpdateEvent;
import com.into.space.assessment.transfer.service.validation.FundsTransferValidationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class FundsTransferServiceTest {

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @Mock
    FundsTransferValidationService fundsTransferValidationService;

    @Mock
    AccountService accountService;

    @InjectMocks
    FundsTransferService fundsTransferService;


    @Test
    void shouldChangeTransactionStatusToValidatedWhenSuccessful() throws InterruptedException {

        //given
        ArgumentCaptor<TransactionUpdateEvent> eventCaptor = ArgumentCaptor.forClass(TransactionUpdateEvent.class);

        //when
        fundsTransferService.performTransfer("1234567", 111111L, 333333L, BigDecimal.TEN);

        //then
        verify(applicationEventPublisher, times(1))
                .publishEvent(eventCaptor.capture());
        verify(accountService, times(1))
                .transfer("1234567", 111111L, 333333L, BigDecimal.TEN);

        TransactionUpdateEvent capturedValue = eventCaptor.getValue();
        Assertions.assertThat(capturedValue.getStatus()).isEqualTo(Status.VALIDATED);
    }

    @Test
    void shouldChangeTransactionStatusToFailedWhenFailedValidation() {

        //given
        String expectedMessage = "Insufficient funds";

        ArgumentCaptor<TransactionUpdateEvent> eventCaptor = ArgumentCaptor.forClass(TransactionUpdateEvent.class);

        doThrow(new InsufficientFundsException(expectedMessage)).when(fundsTransferValidationService).validateFundsTransfer(any(), any(), any());

        //when
        fundsTransferService.performTransfer("1234567", 111111L, 333333L, BigDecimal.TEN);

        //then
        verify(applicationEventPublisher, times(1))
                .publishEvent(eventCaptor.capture());
        verifyNoInteractions(accountService);

        TransactionUpdateEvent capturedValue = eventCaptor.getValue();
        Assertions.assertThat(capturedValue.getStatus()).isEqualTo(Status.FAILED);
        Assertions.assertThat(capturedValue.getDescription()).isEqualTo(expectedMessage);
    }
}