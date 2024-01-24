package com.dskora.serverless.application.dto;

public class UpdateApplicationStatusResponse {
    private Long id;

    public UpdateApplicationStatusResponse() {}

    public UpdateApplicationStatusResponse(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
