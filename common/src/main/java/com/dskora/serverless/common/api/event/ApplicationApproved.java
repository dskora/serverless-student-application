package com.dskora.serverless.common.api.event;

public class ApplicationApproved implements Event {
    private Long id;

    public ApplicationApproved(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
