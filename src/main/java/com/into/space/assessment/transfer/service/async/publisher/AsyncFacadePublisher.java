package com.into.space.assessment.transfer.service.async.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncFacadePublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Async
    public <T extends ApplicationEvent> void publish(T event) {
        applicationEventPublisher.publishEvent(event);
    }
}
