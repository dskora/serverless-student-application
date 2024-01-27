package com.dskora.serverless.student;

import com.dskora.serverless.common.api.event.ApplicationApproved;
import com.dskora.serverless.common.api.event.StudentRegistered;
import com.dskora.serverless.common.service.EventStreamBridge;
import com.dskora.serverless.student.repository.StudentRepository;
import com.dskora.serverless.student.dto.RegisterStudentResponse;
import com.dskora.serverless.student.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentFunctions {
    private EventStreamBridge eventStreamBridge;

    private StudentRepository repository;

    public StudentFunctions(EventStreamBridge eventStreamBridge, StudentRepository repository) {
        this.eventStreamBridge = eventStreamBridge;
        this.repository = repository;
    }
    
    @Bean
    public Function<ApplicationApproved, RegisterStudentResponse> registerStudent() {
        return request -> {
            Student student = Student.register(request.getFirstname(), request.getSurname(), request.getCourseId());

            this.repository.save(student);
            eventStreamBridge.send("events-out-0", new StudentRegistered(student.getId(), request.getFirstname(), request.getSurname(), request.getCourseId()));

            return new RegisterStudentResponse(student.getId());
        };
    }
}
