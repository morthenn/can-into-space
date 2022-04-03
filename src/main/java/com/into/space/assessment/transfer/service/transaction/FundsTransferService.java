package com.into.space.assessment.transfer.service.transaction;

import com.into.space.assessment.account.service.AccountService;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.service.async.event.TransactionUpdateEvent;
import com.into.space.assessment.transfer.service.validation.FundsTransferValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundsTransferService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final FundsTransferValidationService fundsTransferValidationService;

    private final AccountService accountService;


    public void performTransfer(String transactionGuardId, Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        log.info("Performing funds transfer from account {} to account {}", sourceAccountId, targetAccountId);
        try {
            validateFundsTransfer(transactionGuardId, sourceAccountId, targetAccountId, amount);
            accountService.transfer(transactionGuardId, sourceAccountId, targetAccountId, amount);
            log.info("Transfer handled successfully");
        } catch (Exception ex) {
            log.error("Exception occurred during transfer from account{}, to account {}, {} - {}", sourceAccountId, targetAccountId, ex.getMessage(), ex);
            applicationEventPublisher.publishEvent(new TransactionUpdateEvent(this, transactionGuardId, Status.FAILED, ex.getMessage()));
        }
    }

    private void validateFundsTransfer(String transactionGuardId, Long sourceAccountId, Long targetAccountId, BigDecimal amount) throws InterruptedException {
        log.info("Validating funds transfer");
        Thread.sleep(700);
        fundsTransferValidationService.validateFundsTransfer(sourceAccountId, targetAccountId, amount);
        applicationEventPublisher.publishEvent(
                new TransactionUpdateEvent(this, transactionGuardId, Status.VALIDATED, null));
        log.info("Funds transfer validated successfully");
    }
}
