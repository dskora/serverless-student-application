package com.dskora.serverless.application;

import com.dskora.serverless.application.dto.*;
import com.dskora.serverless.application.vo.ApplicationStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
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

    @FunctionName("update-application-status")
    public UpdateApplicationStatusResponse updateApplicationStatusFunc(
        @HttpTrigger(name = "updateApplicationStatusDto",
            route = "applications/{id:long}/status",
            methods = { HttpMethod.PATCH },
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> requestDto,
        @BindingName("id") Long id,
        ExecutionContext context) throws JsonProcessingException {

        String requestBody = requestDto.getBody().orElseThrow();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(requestBody);

        UpdateApplicationStatusRequest request = new UpdateApplicationStatusRequest(id, ApplicationStatus.valueOf(jsonNode.get("status").asText().toUpperCase()));
        Function<UpdateApplicationStatusRequest, UpdateApplicationStatusResponse> function = functionCatalog.lookup("updateApplicationStatus");

        return function.apply(request);
    }

    @FunctionName("find-application")
    public ApplicationResponse findApplicationFunc(
        @HttpTrigger(name = "findApplication",
            route = "applications/{id:long}",
            methods = { HttpMethod.GET },
            authLevel = AuthorizationLevel.ANONYMOUS)
        @BindingName("id") Long id,
        ExecutionContext context) {

        Function<Long, ApplicationResponse> function = functionCatalog.lookup("findApplication");

        return function.apply(id);
    }
}