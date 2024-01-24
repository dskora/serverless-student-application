package com.dskora.serverless.application;

import com.dskora.serverless.application.dto.RegisterApplicationRequest;
import com.dskora.serverless.application.dto.RegisterApplicationResponse;
import com.dskora.serverless.application.model.Application;
import com.dskora.serverless.application.repository.ApplicationRepository;
import com.dskora.serverless.common.api.event.ApplicationRegistered;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ApplicationFunctions {
    private StreamBridge streamBridge;

    private ApplicationRepository repository;

    public ApplicationFunctions(StreamBridge streamBridge, ApplicationRepository repository) {
        this.streamBridge = streamBridge;
        this.repository = repository;
    }
    
    @Bean
    public Function<RegisterApplicationRequest, RegisterApplicationResponse> registerApplication() {
        return request -> {
            Application application = Application.register(request.getFirstname(), request.getSurname(), request.getCourseId());

            repository.save(application);
            streamBridge.send("applications-out-0", new ApplicationRegistered(application.getId(), request.getFirstname(), request.getSurname(), request.getCourseId()));

            return new RegisterApplicationResponse(application.getId());
        };
    }
}
