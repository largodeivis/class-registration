package com.academic.classregistration.jpa;

import com.academic.classregistration.model.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
