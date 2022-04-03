package com.into.space.assessment.transfer.service;


import com.into.space.assessment.transfer.domain.TransactionGuard;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.storage.guard.TransactionGuardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class TransactionGuardServiceTest {

    private static final String TRANSACTION_GUARD_ID = "123456asd";

    @InjectMocks
    TransactionGuardService transactionGuardService;

    @Mock
    TransactionGuardRepository transactionGuardRepository;

    @Test
    void shouldCreateNewTransactionGuard() {
        //given
        TransactionGuard transactionGuard = TransactionGuard.createWithStatus(Status.STARTED);
        TransactionGuard expectedTransactionGuard = new TransactionGuard(TRANSACTION_GUARD_ID, Status.STARTED, null);
        when(transactionGuardRepository.save(transactionGuard))
                .thenReturn(
                        Mono.just(transactionGuard)
                                .map(transactionGuard1 -> {
                                    transactionGuard1.setId(TRANSACTION_GUARD_ID);
                                    return transactionGuard1;
                                }));


        //when
        Mono<TransactionGuard> transactionGuardMono = transactionGuardService.registerNewTransaction();

        //then
        StepVerifier
                .create(transactionGuardMono)
                .consumeNextWith(transactionGuard1 -> {
                    assertEquals(transactionGuard1, expectedTransactionGuard);
                })
                .verifyComplete();
    }

    @Test
    void shouldUpdateExistingTransaction() {
        //given
        TransactionGuard newTransactionGuard = new TransactionGuard(TRANSACTION_GUARD_ID, Status.STARTED, null);
        String sampleExceptionDescription = "sample exception description";
        TransactionGuard updatedTransactionGuard = new TransactionGuard(TRANSACTION_GUARD_ID, Status.FAILED, sampleExceptionDescription);

        when(transactionGuardRepository.getById(TRANSACTION_GUARD_ID))
                .thenReturn(Mono.just(newTransactionGuard));

        when(transactionGuardRepository.save(any()))
                .thenReturn(Mono.just(updatedTransactionGuard));

        //when
        Mono<TransactionGuard> transactionGuardMono = transactionGuardService
                .updateTransactionGuardStatus(TRANSACTION_GUARD_ID, Status.FAILED, sampleExceptionDescription);

        //then
        StepVerifier
                .create(transactionGuardMono)
                .consumeNextWith(transactionGuard1 -> {
                    assertThat(transactionGuard1).isNotNull();
                    assertThat(transactionGuard1.getId()).isEqualTo(TRANSACTION_GUARD_ID);
                    assertThat(transactionGuard1.getStatus()).isEqualTo(Status.FAILED);
                    assertThat(transactionGuard1.getDescription()).isEqualTo(sampleExceptionDescription);
                })
                .verifyComplete();
    }
}