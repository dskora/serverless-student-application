package com.dskora.serverless.common.service;

import com.dskora.serverless.common.api.event.DomainEvent;
import com.dskora.serverless.common.api.event.StreamEvent;
import org.springframework.cloud.stream.function.StreamBridge;

public class EventStreamBridge {
    private StreamBridge streamBridge;

    public EventStreamBridge(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void send(String bindingName, DomainEvent event) {
        this.streamBridge.send(bindingName, StreamEvent.create(event));
    }
}
