package com.dskora.application;

import com.dskora.application.dto.RegisterApplicationRequest;
import com.dskora.application.dto.RegisterApplicationResponse;
import com.dskora.application.model.Application;
import com.dskora.application.repository.ApplicationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ApplicationFunctions {
    private ApplicationRepository repository;

    public ApplicationFunctions(ApplicationRepository repository) {
        this.repository = repository;
    }
    
    @Bean
    public Function<RegisterApplicationRequest, RegisterApplicationResponse> registerApplication() {
        return request -> {
            Application application = Application.register(request.getFirstname(), request.getSurname(), request.getCourseId());
            this.repository.save(application);

            return new RegisterApplicationResponse(application.getId());
        };
    }
}
