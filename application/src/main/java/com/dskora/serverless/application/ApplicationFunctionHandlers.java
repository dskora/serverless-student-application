package com.dskora.serverless.application;

import com.dskora.serverless.application.dto.RegisterApplicationRequest;
import com.dskora.serverless.application.dto.RegisterApplicationResponse;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class ApplicationFunctionHandlers {
    @Autowired
    private FunctionCatalog functionCatalog;

    @FunctionName("register-application")
    public RegisterApplicationResponse registerApplicationFunc(
        @HttpTrigger(name = "registerApplicationDto",
            route = "applications",
            methods = { HttpMethod.POST },
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<RegisterApplicationRequest>> requestDto,
        ExecutionContext context) {

        RegisterApplicationRequest request = requestDto.getBody().orElseThrow();
        Function<RegisterApplicationRequest, RegisterApplicationResponse> function = functionCatalog.lookup("registerApplication");

        return function.apply(request);
    }
}