package com.dskora.serverless.common.api.event;

public class ApplicationRegistered implements DomainEvent {
    private Long id;

    private String firstname;

    private String surname;

    private Long courseId;

    public ApplicationRegistered(Long id, String firstname, String surname, Long courseId) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.courseId = courseId;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getId() {
        return id;
    }
}
