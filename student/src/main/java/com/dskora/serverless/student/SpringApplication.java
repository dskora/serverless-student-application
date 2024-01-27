package com.dskora.serverless.student;

import com.dskora.serverless.common.service.EventStreamBridge;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }

    @Bean
    public EventStreamBridge eventStreamBridge(StreamBridge streamBridge) {
        return new EventStreamBridge(streamBridge);
    }
}