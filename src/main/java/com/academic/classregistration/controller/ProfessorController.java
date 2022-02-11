package com.academic.classregistration.controller;

import com.academic.classregistration.exception.NonUniqueCourseNumberException;
import com.academic.classregistration.model.Course;
import com.academic.classregistration.model.Professor;
import com.academic.classregistration.model.ProfessorCourse;
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
    public ResponseEntity<Object> createProfessor(@RequestBody Professor professor) {
        Professor newProfessor = professorService.createProfessor(professor);
        return new ResponseEntity<>(newProfessor, HttpStatus.CREATED);
    }

    @GetMapping("professors")
    public ResponseEntity<Object> getProfessors() {
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

    //might wanna move this to Courses
//    @PatchMapping("professors")
//    public ResponseEntity<Object> assignProfessorToCourse(@RequestBody ProfessorCourse professorCourse) {
//        Long professorId = professorCourse.getProfessorId();
//        Long courseId = professorCourse.getCourseId();
//        try {
//            Professor professor = professorService.assignProfessorToCourse(professorId, courseId);
//            return new ResponseEntity<>(professor, HttpStatus.OK);
//        } catch (Exception exception){
//            logger.error(exception.getMessage());
//            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//    }
}
