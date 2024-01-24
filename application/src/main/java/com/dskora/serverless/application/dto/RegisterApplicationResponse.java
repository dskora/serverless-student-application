package com.dskora.serverless.application.dto;

public class RegisterApplicationResponse {
    private Long id;

    public RegisterApplicationResponse() {}

    public RegisterApplicationResponse(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
