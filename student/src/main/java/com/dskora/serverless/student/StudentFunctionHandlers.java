package com.dskora.serverless.student;

import com.dskora.serverless.common.api.event.ApplicationApproved;
import com.dskora.serverless.common.api.event.ApplicationRegistered;
import com.dskora.serverless.student.dto.RegisterStudentResponse;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StudentFunctionHandlers {
    @Autowired
    private FunctionCatalog functionCatalog;

    @FunctionName("register-student")
    public RegisterStudentResponse registerStudentFunc(
        @EventHubTrigger(eventHubName = "applications",
            name = "applicationRegisteredTrigger",
            connection = "EVENT_HUBS_CONNECTION_STRING_INCOME",
            cardinality = Cardinality.ONE)
        ApplicationApproved event,
        ExecutionContext context) {

        System.out.println(event.getId());
        Function<ApplicationApproved, RegisterStudentResponse> function = functionCatalog.lookup("registerStudent");

        return function.apply(event);
    }
}