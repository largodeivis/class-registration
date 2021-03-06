package com.academic.classregistration.controller;


import com.academic.classregistration.exception.NonUniqueCourseNumberException;
import com.academic.classregistration.model.Course;
import com.academic.classregistration.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @PostMapping("courses")
    public ResponseEntity<Object> createCourse(@RequestBody Course course) {
        try {
            Course newCourse = courseService.createCourse(course);
            return new ResponseEntity<>(newCourse, HttpStatus.CREATED);
        } catch (NonUniqueCourseNumberException exception) {
            logger.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("courses")
    public ResponseEntity<Object> getCourses() {
        return new ResponseEntity<>(courseService.getCourses(), HttpStatus.OK);
    }


    @GetMapping("courses/{id}")
    public ResponseEntity<Object> getCourse(@PathVariable Long id){
        try {
            Course course = courseService.getCourse(id);
            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (EntityNotFoundException exception){
            logger.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("courses/{courseId}/professor/{professorId}")
    public ResponseEntity<Object> assignProfessor(@PathVariable Long courseId, @PathVariable Long professorId){
        try {
            Course updatedCourse = courseService.assignProfessor(courseId, professorId);
            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
        } catch (EntityNotFoundException exception){
            logger.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("courses/{courseId}/student/{studentId}")
    public ResponseEntity<Object> registerStudent(@PathVariable Long courseId, @PathVariable Long studentId) throws Exception {
        try {
            Course course = courseService.getCourse(courseId);
            Course updatedCourse = courseService.studentCourseRegistration(course, studentId);
            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
        } catch (EntityNotFoundException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
        }
    }
}
