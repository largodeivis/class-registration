package com.academic.classregistration.service;

import com.academic.classregistration.jpa.ProfessorRepository;
import com.academic.classregistration.model.Course;
import com.academic.classregistration.model.Professor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProfessorService {

    private static final Logger logger = LoggerFactory.getLogger(ProfessorService.class);

    @Autowired
    private ProfessorRepository professorRepository;

    public Professor createProfessor(Professor professor){
        professorRepository.save(professor);
        return professor;
    }

    public Professor getProfessor(Long id) {
        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isPresent()) {
            logger.info("Retrieved professor ID:" + id);
            return professor.get();
        } else {
            String errorString = "professor with ID" + id + " not found.";
            logger.error(errorString);
            throw new EntityNotFoundException(errorString);
        }
    }

    public Iterable<Professor> getProfessors(){
        return professorRepository.findAll();
    }


}
