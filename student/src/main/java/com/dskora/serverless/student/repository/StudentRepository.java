package com.dskora.serverless.student.repository;

import com.dskora.serverless.student.model.Student;
import org.springframework.data.repository.ListCrudRepository;

public interface StudentRepository extends ListCrudRepository<Student, Long> {
}
