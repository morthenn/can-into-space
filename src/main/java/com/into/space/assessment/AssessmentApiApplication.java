package com.into.space.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableAsync
@EnableWebFlux
public class AssessmentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssessmentApiApplication.class, args);
    }

}
