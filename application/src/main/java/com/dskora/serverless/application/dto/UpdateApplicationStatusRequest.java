package com.dskora.serverless.application.dto;

import com.dskora.serverless.application.vo.ApplicationStatus;

public class UpdateApplicationStatusRequest {
    private Long id;

    private ApplicationStatus status;

    public UpdateApplicationStatusRequest(Long id, ApplicationStatus status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public ApplicationStatus getStatus() {
        return status;
    }
}
