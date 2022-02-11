package com.academic.classregistration.controller;


import com.academic.classregistration.exception.NonUniqueCourseNumberException;
import com.academic.classregistration.model.Course;
import com.academic.classregistration.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;

@RestController
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @PostMapping("courses")
    public ResponseEntity<Object> createCourse(@RequestBody Course course) {
        try {
            Course newCourse = courseService.createCourse(course);
            return new ResponseEntity<>(newCourse, HttpStatus.OK);
        } catch (NonUniqueCourseNumberException exception) {
            logger.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
