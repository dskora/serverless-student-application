package com.dskora.serverless.student.dto;

public class RegisterStudentResponse {
    private Long id;

    public RegisterStudentResponse() {}

    public RegisterStudentResponse(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
