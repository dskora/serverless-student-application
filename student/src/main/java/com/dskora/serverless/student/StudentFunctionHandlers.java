package com.dskora.serverless.student;

import com.dskora.serverless.common.api.event.ApplicationApproved;
import com.dskora.serverless.common.api.event.ApplicationRegistered;
import com.dskora.serverless.student.dto.RegisterStudentRequest;
import com.dskora.serverless.student.dto.RegisterStudentResponse;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.stereotype.Component;

import java.util.Optional;
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
}