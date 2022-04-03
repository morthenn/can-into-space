package com.into.space.assessment.transfer.api;

import com.into.space.assessment.transfer.api.command.FundsTransferCommand;
import com.into.space.assessment.transfer.domain.TransactionGuard;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.service.FundsTransferFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(FundsController.class)
class FundsControllerApiTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private FundsTransferFacade fundsTransferFacade;

    @Test
    void shouldSuccessfullyReturnStartedTransactionGuard() {
        //given
        FundsTransferCommand transferCommand = new FundsTransferCommand(111111L, 222222L, BigDecimal.ONE);
        TransactionGuard transactionGuard = TransactionGuard.createWithStatus(Status.STARTED);
        transactionGuard.setId("6248b262f69b6006a43300ee");

        Mono<TransactionGuard> transactionGuardMono = Mono.just(transactionGuard);

        when(fundsTransferFacade.transfer(transferCommand)).thenReturn(transactionGuardMono);

        //when, then
        webTestClient.post()
                .uri("/api/v1/account/transfer-funds")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(transferCommand))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TransactionGuard.class);

    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyNotValid() {
        //given
        BigDecimal negativeAmount = BigDecimal.valueOf(-500);
        FundsTransferCommand transferCommand = new FundsTransferCommand(1L, 2L, negativeAmount);
        TransactionGuard transactionGuard = TransactionGuard.createWithStatus(Status.STARTED);
        transactionGuard.setId("6248b262f69b6006a43300ee");

        when(fundsTransferFacade.transfer(any())).thenThrow(new RuntimeException(("sample exception")));

        //when, then
        webTestClient.post()
                .uri("/api/v1/account/transfer-funds")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(transferCommand))
                .exchange()
                .expectStatus().is4xxClientError();

    }

    @Test
    void shouldReturnInternalServerErrorOnFailure() {
        //given
        FundsTransferCommand transferCommand = new FundsTransferCommand(111111L, 222222L, BigDecimal.ONE);
        TransactionGuard transactionGuard = TransactionGuard.createWithStatus(Status.STARTED);
        transactionGuard.setId("6248b262f69b6006a43300ee");

        when(fundsTransferFacade.transfer(any())).thenThrow(new RuntimeException(("sample exception")));

        //when, then
        webTestClient.post()
                .uri("/api/v1/account/transfer-funds")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(transferCommand))
                .exchange()
                .expectStatus().is5xxServerError();

    }

}