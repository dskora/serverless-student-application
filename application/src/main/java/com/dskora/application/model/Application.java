package com.dskora.application.model;

import jakarta.persistence.*;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    private String surname;

    private Long courseId;

    protected Application() {}

    private Application(String firstname, String surname, Long courseId) {
        this.firstname = firstname;
        this.surname = surname;
        this.courseId = courseId;
    }

    public static Application register(String firstname, String surname, Long courseId) {
        Application application = new Application(firstname, surname, courseId);

        return application;
    }

    public Long getId() {
        return id;
    }
}
