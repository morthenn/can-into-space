package com.into.space.assessment.transfer.service;

import com.into.space.assessment.transfer.domain.Transaction;
import com.into.space.assessment.transfer.domain.enumeration.Operation;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.storage.business.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.into.space.assessment.transfer.domain.enumeration.Status.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService;

    @Test
    void shouldUpdateTransactionStatus() {

        //given
        LocalDateTime now = LocalDateTime.now();
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        Transaction transaction = Transaction.builder()
                .startDateTime(now)
                .fromAccountId(111111L)
                .toAccountId(222222L)
                .operation(Operation.TRANSFER)
                .status(Status.STARTED)
                .amount(BigDecimal.TEN)
                .transactionGuardId("123")
                .build();

        when(transactionRepository.getByTransactionGuardId("123")).thenReturn(transaction);
        when(transactionRepository.save(transactionCaptor.capture())).thenReturn(any());

        //when
        transactionService.update("123", SUCCESS);

        //then
        Transaction capturedTransaction = transactionCaptor.getValue();
        assertThat(capturedTransaction)
                .extracting("startDateTime", "fromAccountId", "toAccountId", "operation", "amount", "transactionGuardId")
                .doesNotContainNull()
                .containsExactlyInAnyOrder(now, 111111L, 222222L, Operation.TRANSFER, BigDecimal.TEN, "123");
        assertThat(capturedTransaction.getStatus()).isEqualTo(SUCCESS);
    }
}