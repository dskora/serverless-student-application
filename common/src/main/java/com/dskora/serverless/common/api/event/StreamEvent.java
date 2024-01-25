package com.dskora.serverless.common.api.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StreamEvent {
    private Long id;

    private String type;

    private String payload;

    protected StreamEvent(Long id, String type, String payload) {
        this.id = id;
        this.type = type;
        this.payload = payload;
    }

    public static StreamEvent create(DomainEvent event) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        return new StreamEvent(event.getId(), event.getClass().getSimpleName(), json);
    }

    public Long getId() {
        return id;
    }
}
