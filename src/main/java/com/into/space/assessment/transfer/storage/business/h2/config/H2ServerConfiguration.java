package com.into.space.assessment.transfer.storage.business.h2.config;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static org.h2.tools.Server.createWebServer;

/**
 * Need to manually handle H2ServerConfiguration because it's not provided with webflux/netty
 */
@Component
@Slf4j
public class H2ServerConfiguration {


    @Value("${h2-server.port}")
    Integer h2ConsolePort;
    private Server webServer;

    @EventListener(ContextRefreshedEvent.class)
    public void start() throws java.sql.SQLException {
        log.info("Starting h2 console at port " + h2ConsolePort);
        this.webServer = createWebServer("-webPort", h2ConsolePort.toString(),
                "-tcpAllowOthers").start();
        log.info("H2 Console started, available here -> " + webServer.getURL());
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        log.info("stopping h2 console at port " + h2ConsolePort);
        this.webServer.stop();
    }
}

