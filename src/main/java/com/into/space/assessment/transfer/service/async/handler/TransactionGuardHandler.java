package com.into.space.assessment.transfer.service.async.handler;

import com.into.space.assessment.transfer.service.TransactionGuardService;
import com.into.space.assessment.transfer.service.async.event.TransactionUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionGuardHandler {

    private final TransactionGuardService transactionGuardService;

    @EventListener(TransactionUpdateEvent.class)
    public void updateTransactionGuard(TransactionUpdateEvent transactionUpdateEvent) {
        log.info("Handling TransactionUpdateEvent in TransactionGuard");
        transactionGuardService.updateTransactionGuardStatus(transactionUpdateEvent.getId(), transactionUpdateEvent.getStatus(), transactionUpdateEvent.getDescription());
    }
}
