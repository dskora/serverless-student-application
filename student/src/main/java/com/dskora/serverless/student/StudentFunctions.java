package com.dskora.serverless.student;

import com.dskora.serverless.common.api.event.StudentRegistered;
import com.dskora.serverless.common.service.EventStreamBridge;
import com.dskora.serverless.student.dto.RegisterStudentRequest;
import com.dskora.serverless.student.dto.StudentResponse;
import com.dskora.serverless.student.repository.StudentRepository;
import com.dskora.serverless.student.dto.RegisterStudentResponse;
import com.dskora.serverless.student.model.Student;
import jakarta.persistence.EntityNotFoundException;
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
    public Function<RegisterStudentRequest, RegisterStudentResponse> registerStudent() {
        return request -> {
            Student student = Student.register(request.getFirstname(), request.getSurname(), request.getCourseId());

            this.repository.save(student);
            eventStreamBridge.send("events-out-0", new StudentRegistered(student.getId(), request.getFirstname(), request.getSurname(), request.getCourseId()));

            return new RegisterStudentResponse(student.getId());
        };
    }

    @Bean
    public Function<Long, StudentResponse> findStudent() {
        return studentId -> {
            Student student = repository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + studentId));

            return new StudentResponse(student.getId(), student.getFirstname(), student.getSurname(), student.getCourseId());
        };
    }
}
