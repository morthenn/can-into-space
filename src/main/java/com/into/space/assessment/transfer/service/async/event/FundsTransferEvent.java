package com.into.space.assessment.transfer.service.async.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Getter
@Value
public class FundsTransferEvent extends ApplicationEvent {

    Long sourceAccountId;

    Long targetAccountId;

    String transactionGuardId;

    BigDecimal amount;

    public FundsTransferEvent(Object source, Long sourceAccountId, Long targetAccountId, String transactionGuardId, BigDecimal amount) {
        super(source);
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.transactionGuardId = transactionGuardId;
        this.amount = amount;
    }
}
