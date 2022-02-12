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

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private ProfessorService professorService;
    private StudentService studentService;
    private CourseRepository courseRepository;

    @Autowired
    CourseService(CourseRepository courseRepository, ProfessorService professorService, StudentService studentService){
        this.courseRepository = courseRepository;
        this.professorService = professorService;
        this.studentService = studentService;
    }

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

    public Course registerStudent(Course course, Long studentId){
        Student student = studentService.getStudent(studentId);
        course.registerStudent(student);
        courseRepository.save(course);
        return course;
    }

    public Course unregisterStudent(Course course, Long studentId){
        Student student = studentService.getStudent(studentId);
        Course updatedCourse = course.unregisterStudent(student);
        studentService.unregisterCourse(student, course);
        return updatedCourse;
    }

    public Course studentCourseRegistration(Course course, Long studentId) throws Exception {
        Course updatedCourse;
        Student updatedStudent;
        String errorMessage = "Student ID: " + studentId + " not found. Unable to register to Course.";
        try {
            updatedCourse = registerStudent(course, studentId);
        } catch (EntityNotFoundException exception) {
            logger.error(exception.getMessage());
            throw new EntityNotFoundException(errorMessage);
        }
        try {
            updatedStudent = studentService.registerCourse(studentId, course);
        } catch(Exception exception) {
            unregisterStudent(course, studentId);
            logger.error(exception.getMessage());
            throw new Exception(errorMessage + "\nReverting changes made to Course ID: " + updatedCourse.getId());
        }
        return updatedCourse;
    }
}
