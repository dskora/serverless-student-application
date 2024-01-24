package com.dskora.serverless.application;

import com.dskora.serverless.application.dto.*;
import com.dskora.serverless.application.model.Application;
import com.dskora.serverless.application.repository.ApplicationRepository;
import com.dskora.serverless.common.api.event.ApplicationApproved;
import com.dskora.serverless.common.api.event.ApplicationRegistered;
import com.dskora.serverless.common.api.event.ApplicationRejected;
import com.dskora.serverless.common.api.event.Event;
import jakarta.persistence.EntityNotFoundException;
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

    @Bean
    public Function<UpdateApplicationStatusRequest, UpdateApplicationStatusResponse> updateApplicationStatus() {
        return request -> {
            Application application = repository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Application not found" + request.getId()));

            Event event = null;
            switch (request.getStatus()) {
                case APPROVED -> {
                    application.approve();
                    event = new ApplicationApproved(application.getId());
                }
                case REJECTED -> {
                    application.reject();
                    event = new ApplicationRejected(application.getId());
                }
            }

            if (event != null) {
                repository.save(application);
                streamBridge.send("applications-out-0", event);
            }

            return new UpdateApplicationStatusResponse(application.getId());
        };
    }

    @Bean
    public Function<Long, ApplicationResponse> findApplication() {
        return applicationId -> {
            Application application = repository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found: " + applicationId));

            return new ApplicationResponse(application.getId(), application.getFirstname(), application.getSurname(), application.getCourseId(), application.getStatus().toString().toLowerCase());
        };
    }
}