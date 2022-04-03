package com.into.space.assessment.transfer.service.async.handler;

import com.into.space.assessment.transfer.domain.Transaction;
import com.into.space.assessment.transfer.domain.enumeration.Operation;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.service.TransactionService;
import com.into.space.assessment.transfer.service.async.event.TransactionStartedEvent;
import com.into.space.assessment.transfer.service.async.event.TransactionUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionLifecycleHandler {

    private final TransactionService transactionService;

    @EventListener(TransactionStartedEvent.class)
    public void handleTransactionUpdate(TransactionStartedEvent transactionStartedEvent) {
        log.info("Handling TransactionStartedEvent in TransactionLifecycleHandler");

        Transaction transaction = Transaction.builder()
                .startDateTime(transactionStartedEvent.getStartDateTime())
                .fromAccountId(transactionStartedEvent.getSenderAccountId())
                .toAccountId(transactionStartedEvent.getReceiverAccountId())
                .operation(Operation.TRANSFER)
                .status(Status.STARTED)
                .amount(transactionStartedEvent.getAmount())
                .transactionGuardId(transactionStartedEvent.getTransactionGuardId())
                .build();

        transactionService.create(transaction);
    }

    @EventListener(TransactionUpdateEvent.class)
    public void handleTransactionUpdate(TransactionUpdateEvent transactionUpdateEvent) {
        log.info("Handling TransactionStartedEvent in TransactionLifecycleHandler");

        transactionService.update(transactionUpdateEvent.getId(), transactionUpdateEvent.getStatus());
    }
}
