package com.academic.classregistration.controller;

import com.academic.classregistration.model.Professor;
import com.academic.classregistration.service.ProfessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
public class ProfessorController {
    private static final Logger logger = LoggerFactory.getLogger(ProfessorController.class);

    @Autowired
    private ProfessorService professorService;

    @PostMapping("professors")
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor) {
        Professor newProfessor = professorService.createProfessor(professor);
        return new ResponseEntity<>(newProfessor, HttpStatus.CREATED);
    }

    @GetMapping("professors")
    public ResponseEntity<Iterable<Professor>> getProfessors() {
        return new ResponseEntity<>(professorService.getProfessors(), HttpStatus.OK);
    }

    @GetMapping("professors/{id}")
    public ResponseEntity<Object> getProfessor(@PathVariable Long id){
        try {
            Professor professor = professorService.getProfessor(id);
            return new ResponseEntity<>(professor, HttpStatus.OK);
        } catch (EntityNotFoundException exception){
            logger.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
        }
    }
}
