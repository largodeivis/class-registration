package com.academic.classregistration.service;

import com.academic.classregistration.exception.NonUniqueCourseNumberException;
import com.academic.classregistration.jpa.CourseRepository;
import com.academic.classregistration.model.Course;
import com.academic.classregistration.model.Professor;
import com.academic.classregistration.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StudentService studentService;

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
            logger.info("Retrieved course ID:" + id);
            return course.get();
        } else {
            String errorString = "Course with ID: " + id +" not found.";
            logger.error(errorString);
            throw new EntityNotFoundException(errorString);
        }
    }

    public Course getCourse(String courseNumber){
        Optional<Course> course = courseRepository.findByCourseNumber(courseNumber);
        if (course.isPresent()){
            logger.info("Retrieved course with Course Number:" + courseNumber);
            return course.get();
        } else {
            String errorString = "Course with Course Number:" + courseNumber +" not found.";
            logger.error(errorString);
            throw new EntityNotFoundException(errorString);
        }
    }

    public Iterable<Course> getCourses(){
        return courseRepository.findAll();
    }

    public Course assignProfessor(Course course, Long professorId){
        Professor professor = professorService.getProfessor(professorId);
        course.setProfessor(professor);
        courseRepository.save(course);
        return course;
    }

    public Course assignProfessor(Long courseId, Long professorId){
        Course course = getCourse(courseId);
        Professor professor = professorService.getProfessor(professorId);
        course.setProfessor(professor);
        courseRepository.save(course);
        return course;
    }

    public Course registerStudent(Course course, Student student){
        course.registerStudent(student);
        courseRepository.save(course);
        return course;
    }

    public Course registerStudent(Course course, Long studentId){
        Student student = studentService.getStudent(studentId);
        course.registerStudent(student);
        courseRepository.save(course);
        return course;
    }

    public Course unregisterStudent(Course course, Long studentId){
        Student student = studentService.getStudent(studentId);
        Course updatedCourse = course.unregisterStudent(student);
        return updatedCourse;
    }

    public Course studentCourseRegistration(Course course, Long studentId) throws EntityNotFoundException{
        Course updatedCourse;
        Student updatedStudent;
        try {
            updatedCourse = registerStudent(course, studentId);
        } catch (EntityNotFoundException exception) {
            String errorMessage = "Error while updating course ID: " + course.getId();
            logger.error(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }
        try {
            updatedStudent = studentService.registerStudentToCourse(studentId, course);
        } catch(EntityNotFoundException exception) {
            String errorMessage = "Error while updating student ID: " + studentId + ". Reverting changes to Course ID: " + updatedCourse.getId();
            unregisterStudent(course, studentId);
            logger.error(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }
        return updatedCourse;
    }
}
