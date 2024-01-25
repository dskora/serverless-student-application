package com.dskora.serverless.student;

import com.dskora.serverless.common.api.event.ApplicationApproved;
import com.dskora.serverless.common.api.event.ApplicationRegistered;
import com.dskora.serverless.student.repository.StudentRepository;
import com.dskora.serverless.student.dto.RegisterStudentResponse;
import com.dskora.serverless.student.model.Student;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentFunctions {
    private StreamBridge streamBridge;

    private StudentRepository repository;

    public StudentFunctions(StreamBridge streamBridge, StudentRepository repository) {
        this.streamBridge = streamBridge;
        this.repository = repository;
    }
    
    @Bean
    public Function<ApplicationApproved, RegisterStudentResponse> registerStudent() {
        return event -> {
            Student student = Student.register(event.getFirstname(), event.getSurname(), event.getCourseId());

            this.repository.save(student);
            streamBridge.send("students-out-0", student);

            return new RegisterStudentResponse(student.getId());
        };
    }
}
