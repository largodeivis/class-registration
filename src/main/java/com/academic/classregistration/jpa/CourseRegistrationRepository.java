package com.academic.classregistration.jpa;

import com.academic.classregistration.model.CourseRegistration;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CourseRegistrationRepository extends CrudRepository<CourseRegistration, UUID> {
}
