package com.into.space.assessment.transfer.service.async.handler;

import com.into.space.assessment.transfer.service.async.event.FundsTransferEvent;
import com.into.space.assessment.transfer.service.transaction.FundsTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FundsTransferEventHandler {

    private final FundsTransferService fundsTransferService;

    @EventListener(FundsTransferEvent.class)
    public void handleTransfer(FundsTransferEvent fundsTransferEvent) {
        log.info("Handling FundsTransferEvent");

        fundsTransferService.performTransfer(
                fundsTransferEvent.getTransactionGuardId(),
                fundsTransferEvent.getSourceAccountId(),
                fundsTransferEvent.getTargetAccountId(),
                fundsTransferEvent.getAmount());
    }
}
