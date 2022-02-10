package com.academic.classregistration.jpa;

import com.academic.classregistration.model.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CourseRepository extends CrudRepository<Course, UUID> {
}
