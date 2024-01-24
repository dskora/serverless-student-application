package com.dskora.serverless.application.dto;

public class ApplicationResponse {
    private Long id;

    private String firstname;

    private String surname;

    private Long courseId;

    private String status;

    public ApplicationResponse(Long id, String firstname, String surname, Long courseId, String status) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.courseId = courseId;
        this.status = status;
    }
}
