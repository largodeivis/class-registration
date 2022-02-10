package com.academic.classregistration.jpa;

import com.academic.classregistration.model.Professor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProfessorRepository extends CrudRepository<Professor, UUID> {

}
