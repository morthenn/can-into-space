package com.into.space.assessment.transfer.service;

import com.into.space.assessment.transfer.api.command.FundsTransferCommand;
import com.into.space.assessment.transfer.domain.TransactionGuard;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.service.async.event.FundsTransferEvent;
import com.into.space.assessment.transfer.service.async.event.TransactionStartedEvent;
import com.into.space.assessment.transfer.service.async.publisher.AsyncFacadePublisher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundsTransferFacadeTest {

    @Mock
    AsyncFacadePublisher facadePublisher;

    @Mock
    TransactionGuardService transactionGuardService;

    @InjectMocks
    FundsTransferFacade fundsTransferFacade;

    @Test
    void shouldSuccessfullyInitiateTransferFundsWorkflow() {

        //given
        TransactionGuard expected = new TransactionGuard("1234567", Status.STARTED, null);

        when(transactionGuardService.registerNewTransaction()).thenReturn(Mono.just(expected));

        //when
        Mono<TransactionGuard> transfer = fundsTransferFacade.transfer(new FundsTransferCommand(123L, 3211L, BigDecimal.TEN));

        //then
        StepVerifier.create(transfer).assertNext(transactionGuard1 -> {
                    Assertions.assertThat(transactionGuard1).isEqualTo(expected);
                    verify(facadePublisher, times(1)).publish(any(TransactionStartedEvent.class));
                    verify(facadePublisher, times(1)).publish(any(FundsTransferEvent.class));
                })
                .verifyComplete();
    }
}