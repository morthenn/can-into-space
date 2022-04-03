package com.into.space.assessment.transfer.service.async.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Getter
@Value
public class TransactionStartedEvent extends ApplicationEvent {

    Long senderAccountId;

    Long receiverAccountId;

    String transactionGuardId;

    LocalDateTime startDateTime;

    BigDecimal amount;

    public TransactionStartedEvent(Object source, Long senderAccountId, Long receiverAccountId, String transactionGuardId, LocalDateTime startDateTime,
                                   BigDecimal amount) {
        super(source);
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.transactionGuardId = transactionGuardId;
        this.startDateTime = startDateTime;
        this.amount = amount;
        ;
    }
}
