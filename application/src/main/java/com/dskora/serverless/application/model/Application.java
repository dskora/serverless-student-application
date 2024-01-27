package com.dskora.serverless.application.model;

import com.dskora.serverless.application.vo.ApplicationStatus;
import jakarta.persistence.*;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ApplicationStatus status;

    private String firstname;

    private String surname;

    private Long courseId;

    protected Application() {}

    private Application(String firstname, String surname, Long courseId) {
        this.firstname = firstname;
        this.surname = surname;
        this.courseId = courseId;
        this.status = ApplicationStatus.REGISTERED;
    }

    public static Application register(String firstname, String surname, Long courseId) {
        Application application = new Application(firstname, surname, courseId);

        return application;
    }

    public void approve() {
        status = ApplicationStatus.APPROVED;
    }

    public void reject() {
        status = ApplicationStatus.REJECTED;
    }

    public Long getId() {
        return id;
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

    public ApplicationStatus getStatus() {
        return status;
    }
}
