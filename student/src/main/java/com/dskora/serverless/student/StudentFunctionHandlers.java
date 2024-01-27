package com.dskora.serverless.student;

import com.dskora.serverless.student.dto.RegisterStudentRequest;
import com.dskora.serverless.student.dto.RegisterStudentResponse;
import com.dskora.serverless.student.dto.StudentResponse;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Component
public class StudentFunctionHandlers {
    @Autowired
    private FunctionCatalog functionCatalog;

    @FunctionName("register-student")
    public RegisterStudentResponse registerStudentFunc(
        @HttpTrigger(name = "registerStudentDto",
            route = "students",
            methods = { HttpMethod.POST },
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<RegisterStudentRequest>> requestDto,
        ExecutionContext context) {

        RegisterStudentRequest request = requestDto.getBody().orElseThrow();
        Function<RegisterStudentRequest, RegisterStudentResponse> function = functionCatalog.lookup("registerStudent");

        return function.apply(request);
    }

    @FunctionName("register-student-bulk")
    public Set<RegisterStudentResponse> registerStudentBulkFunc(
        @HttpTrigger(name = "registerStudentDto",
            methods = { HttpMethod.POST },
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<RegisterStudentRequest[]>> requestDto,
        ExecutionContext context) {

        RegisterStudentRequest[] data = requestDto.getBody().orElseThrow();

        Set<RegisterStudentResponse> response = new HashSet<RegisterStudentResponse>();
        for (RegisterStudentRequest item: data) {
            Function<RegisterStudentRequest, RegisterStudentResponse> function = functionCatalog.lookup("registerStudent");
            response.add(function.apply(item));
        }

        return response;
    }

    @FunctionName("find-student")
    public StudentResponse findStudentFunc(
        @HttpTrigger(name = "findStudent",
            route = "students/{id:long}",
            methods = { HttpMethod.GET },
            authLevel = AuthorizationLevel.ANONYMOUS)
        @BindingName("id") Long id,
        ExecutionContext context) {

        Function<Long, StudentResponse> function = functionCatalog.lookup("findStudent");

        return function.apply(id);
    }
}