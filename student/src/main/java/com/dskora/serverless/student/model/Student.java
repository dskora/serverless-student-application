package com.dskora.serverless.student.model;

import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    private String surname;

    private Long courseId;

    protected Student() {}

    private Student(String firstname, String surname, Long courseId) {
        this.firstname = firstname;
        this.surname = surname;
        this.courseId = courseId;
    }

    public static Student register(String firstname, String surname, Long courseId) {
        Student student = new Student(firstname, surname, courseId);

        return student;
    }

    public Long getId() {
        return id;
    }
}
