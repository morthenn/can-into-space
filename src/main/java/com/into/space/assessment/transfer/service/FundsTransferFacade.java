package com.into.space.assessment.transfer.service;

import com.into.space.assessment.transfer.api.command.FundsTransferCommand;
import com.into.space.assessment.transfer.domain.TransactionGuard;
import com.into.space.assessment.transfer.service.async.event.FundsTransferEvent;
import com.into.space.assessment.transfer.service.async.event.TransactionStartedEvent;
import com.into.space.assessment.transfer.service.async.publisher.AsyncFacadePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundsTransferFacade implements FundsTransfer {

    private final TransactionGuardService transactionGuardService;

    private final AsyncFacadePublisher facadePublisher;

    public Mono<TransactionGuard> transfer(FundsTransferCommand fundsTransferCommand) {
        log.info("Starting transfer job");
        return transactionGuardService.registerNewTransaction()
                .flatMap(transactionGuard -> {
                            LocalDateTime now = LocalDateTime.now();

                            facadePublisher.publish(
                                    new TransactionStartedEvent(this,
                                            fundsTransferCommand.getSenderAccountId(),
                                            fundsTransferCommand.getReceiverAccountId(),
                                            transactionGuard.getId(),
                                            now,
                                            fundsTransferCommand.getAmount())
                            );
                            log.info("TransactionStartedEvent sent");

                            facadePublisher.publish(
                                    new FundsTransferEvent(this,
                                            fundsTransferCommand.getSenderAccountId(),
                                            fundsTransferCommand.getReceiverAccountId(),
                                            transactionGuard.getId(),
                                            fundsTransferCommand.getAmount()
                                    )
                            );
                            log.info("FundsTransferEvent sent");
                            return Mono.just(transactionGuard);
                        }
                );
    }
}

