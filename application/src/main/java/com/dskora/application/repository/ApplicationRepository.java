package com.dskora.application.repository;

import com.dskora.application.model.Application;
import org.springframework.data.repository.ListCrudRepository;

public interface ApplicationRepository extends ListCrudRepository<Application, Long> {
}
