package com.dskora.serverless.application.repository;

import com.dskora.serverless.application.model.Application;
import org.springframework.data.repository.ListCrudRepository;

public interface ApplicationRepository extends ListCrudRepository<Application, Long> {
}
