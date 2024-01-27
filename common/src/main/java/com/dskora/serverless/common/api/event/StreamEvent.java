package com.dskora.serverless.common.api.event;

public class StreamEvent {
    private Long id;

    private String type;

    private DomainEvent payload;

    protected StreamEvent(Long id, String type, DomainEvent payload) {
        this.id = id;
        this.type = type;
        this.payload = payload;
    }

    public static StreamEvent create(DomainEvent event) {
        return new StreamEvent(event.getId(), event.getClass().getSimpleName(), event);
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public DomainEvent getPayload() {
        return payload;
    }
}
