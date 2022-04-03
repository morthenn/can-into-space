package com.into.space.assessment.transfer.service.async.event;

import com.into.space.assessment.transfer.domain.enumeration.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

@EqualsAndHashCode(callSuper = false)
@Getter
@Value
public class TransactionUpdateEvent extends ApplicationEvent {

    String id;

    Status status;

    String description;

    public TransactionUpdateEvent(Object source, String id, Status status, String description) {
        super(source);
        this.id = id;
        this.status = status;
        this.description = description;
    }
}
