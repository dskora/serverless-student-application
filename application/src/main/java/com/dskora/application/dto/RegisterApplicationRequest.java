package com.dskora.application.dto;

public class RegisterApplicationRequest {
    private String firstname;

    private String surname;

    private Long courseId;

    public RegisterApplicationRequest(String firstname, String surname, Long courseId) {
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
}
