package com.academic.classregistration.service;

import com.academic.classregistration.exception.NonUniqueCourseNumberException;
import com.academic.classregistration.jpa.CourseRepository;
import com.academic.classregistration.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    public Course createCourse(Course course){
        try {
            courseRepository.save(course);
        } catch (DataIntegrityViolationException exception){
            logger.error(Arrays.toString(exception.getStackTrace()));
            throw new NonUniqueCourseNumberException(course.getCourseNumber());
        }
        return course;
    }

    public Course getCourse(Long id){
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()){
            return course.get();
        } else {
            throw new EntityNotFoundException("Course with ID" + id +" not found.");
        }
    }

    public Course getCourse(String courseNumber){
        Optional<Course> course = courseRepository.findByCourseNumber(courseNumber);
        if (course.isPresent()){
            return course.get();
        } else {
            throw new EntityNotFoundException("Course with Course Number:" + courseNumber +" not found.");
        }
    }

    public Iterable<Course> getCourses(){
        return courseRepository.findAll();
    }
}
