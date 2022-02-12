package com.academic.classregistration.service;

import com.academic.classregistration.exception.NonUniqueCourseNumberException;
import com.academic.classregistration.jpa.CourseRepository;
import com.academic.classregistration.jpa.ProfessorRepository;
import com.academic.classregistration.jpa.StudentRepository;
import com.academic.classregistration.model.Course;
import com.academic.classregistration.model.Professor;
import com.academic.classregistration.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProfessorRepository professorRepository;

    private CourseService courseService;
    private StudentService studentService;
    private ProfessorService professorService;

    Course course1 = new Course(1L, "Mystery Solving", "MYS101");
    Course course2 = new Course(2L, "Cyber Sleuthing", "CYB101");

    Student student1 = new Student(1, "Daphne");
    Student student2 = new Student(2, "Velma");

    Professor professor = new Professor(1, "Scooby Doo");

    List<Course> courseList;
    Set<Student> studentSet;

    @BeforeEach
    public void setUp(){
        studentService = new StudentService(studentRepository);
        professorService = new ProfessorService(professorRepository);
        courseService = new CourseService(courseRepository, professorService, studentService);
        courseList = new ArrayList<>();
        studentSet = new HashSet<>();
        course1.setProfessor(professor);
        course1.registerStudent(student1);
        course1.registerStudent(student2);
        course2.registerStudent(student2);
        courseList.add(course1);
        courseList.add(course2);
        studentSet.add(student1);
        studentSet.add(student2);
    }

    @AfterEach
    public void tearDown(){
        studentService = null;
        professorService = null;
        courseService = null;
        courseList = null;
        studentSet = null;
        student1 = null;
        student2 = null;
        course1 = null;
        course2 = null;
        professor = null;
    }

    @Test
    void createCourseReturnsCourse(){
        when(courseRepository.save(course1)).thenReturn(course1);
        Course course = courseService.createCourse(course1);
        Assertions.assertEquals(course1, course);
    }

//    @Test
//    void createCourseWithSameCourseNumberThrowsException(){
//        String courseNumber = "MYS101";
//        Course invalidCourse = new Course("Mystery Solving II", courseNumber);
//        when(courseRepository.save(course1)).thenReturn(course1);
//        Exception thrown = Assertions.assertThrows(DataIntegrityViolationException.class, () -> courseService.createCourse(invalidCourse));
//        Assertions.assertEquals(thrown.getMessage(), "Unable to save Course. Course Number " + courseNumber + " already exists.");
//    }

    @Test
    void getCourseReturnsCourse(){
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));
        Course course = courseService.getCourse(1L);
        Assertions.assertEquals(course1, course);
    }

    @Test
    void getCourseInvalidIdThrowsEntityNotFoundException(){
        long invalidId = 3;
        Exception thrown = assertThrows(EntityNotFoundException.class, () -> courseService.getCourse(invalidId));
        Assertions.assertEquals(thrown.getMessage(), "Course with ID: " + invalidId + " not found.");
    }

    @Test
    void getCoursesReturnsAllCourses(){
        when(courseRepository.findAll()).thenReturn(courseList);
        Iterable<Course> courses = courseService.getCourses();
        List<Course> iterableList = new ArrayList<>();
        courses.forEach(iterableList::add);
        Assertions.assertEquals(courseList, iterableList);
    }

    @Test
    void assignProfessorReturnsCourseWithProfessor(){
        long professorId = 1;
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course2));
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        Assertions.assertTrue(course2.getProfessor() == null);
        Course course = courseService.assignProfessor(course2.getId(), professorId);
        Assertions.assertTrue(course.getProfessor() == professor);
        Assertions.assertTrue(course2.getProfessor() == professor);
    }

    @Test
    void assignProfessorInvalidIdThrowsException(){
        long professorId = 5;
        Exception thrown = assertThrows(EntityNotFoundException.class, () -> courseService.assignProfessor(course2, professorId));
        Assertions.assertEquals(thrown.getMessage(), "Professor with ID: " + professorId + " not found.");
    }

    @Test
    void registerStudentReturnsCourseWithStudent(){
        long studentId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));
        Assertions.assertFalse(course2.getStudents().contains(student1));
        Course course = courseService.registerStudent(course2, studentId);
        Assertions.assertTrue(course.getStudents().contains(student1));
        Assertions.assertEquals(course, course2);
    }

    @Test
    void registerStudentInvalidIdThrowsException(){
        long studentId = 5;
        Exception thrown = assertThrows(EntityNotFoundException.class, () -> courseService.registerStudent(course2, studentId));
        Assertions.assertEquals(thrown.getMessage(), "Student with ID: " + studentId + " not found.");
    }

    @Test
    void unregisterStudentReturnsSetWithoutStudent(){
        long studentId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));
        Assertions.assertTrue(course1.getStudents().contains(student1));

        Course course = courseService.unregisterStudent(course1, studentId);
        Assertions.assertFalse(course.getStudents().contains(student1));
        Assertions.assertTrue(course.getStudents().contains(student2));
        Assertions.assertEquals(course, course1);
    }

    @Test
    void unregisterStudentInvalidIdThrowsException(){
        long studentId = 5;
        Exception thrown = assertThrows(EntityNotFoundException.class, () -> courseService.unregisterStudent(course1, studentId));
        Assertions.assertEquals(thrown.getMessage(), "Student with ID: " + studentId + " not found.");
    }

}
