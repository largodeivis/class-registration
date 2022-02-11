package com.academic.classregistration.jpa;

import com.academic.classregistration.model.Professor;
import org.springframework.data.repository.CrudRepository;

public interface ProfessorRepository extends CrudRepository<Professor, Long> {

}
