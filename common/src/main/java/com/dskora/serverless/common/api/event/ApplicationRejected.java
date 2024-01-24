package com.dskora.serverless.common.api.event;

public class ApplicationRejected implements Event {
    private Long id;

    public ApplicationRejected(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
