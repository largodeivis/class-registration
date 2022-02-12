package com.academic.classregistration.service;

import com.academic.classregistration.jpa.StudentRepository;
import com.academic.classregistration.model.Course;
import com.academic.classregistration.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    private StudentService studentService;

    Student student1 = new Student(1, "Daphne");
    Student student2 = new Student(2, "Velma");

    Course course1 =  new Course("Mystery Solving", "MYS101");

    List<Student> studentList;
    Set<Course> courseSet;

    @BeforeEach
    public void setUp(){
        studentService = new StudentService(studentRepository);
        studentList = new ArrayList<>();
        courseSet = new HashSet<>();
        student1.addCourse(course1);
        studentList.add(student1);
        studentList.add(student2);
        courseSet.add(course1);
    }

    @AfterEach
    public void tearDown(){
        studentService = null;
        studentList = null;
        student1 = null;
        student2 = null;
        course1 = null;
        courseSet = null;
    }


    @Test
    void createStudentReturnsStudent(){
        when(studentRepository.save(student1)).thenReturn(student1);
        Student student = studentService.createStudent(student1);
        Assertions.assertEquals(student1, student);
    }

    @Test
    void getStudentReturnsStudent(){
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        Student student = studentService.getStudent(1L);
        Assertions.assertEquals(student1, student);
    }

    @Test
    void getStudentInvalidIdThrowsEntityNotFoundException(){
        long invalidId = 3;
        Exception thrown = assertThrows(EntityNotFoundException.class, () -> studentService.getStudent(invalidId));
        Assertions.assertEquals(thrown.getMessage(), "Student with ID: " + invalidId + " not found.");
    }

    @Test
    void getStudentsReturnsAllStudents(){
        when(studentRepository.findAll()).thenReturn(studentList);
        Iterable<Student> students = studentService.getStudents();
        List<Student> iterableList = new ArrayList<>();
        students.forEach(iterableList::add);
        Assertions.assertEquals(studentList, iterableList);
    }

    @Test
    void registerCourseToInvalidStudentIdThrowsEntityNotFoundException() {
        long id = 2;
        when(studentRepository.findById(id)).thenReturn(Optional.of(student2));
        Assertions.assertTrue(student2.getCourses().isEmpty());

        Student student = studentService.registerCourse(id, course1);
        Assertions.assertEquals(student.getCourses(), courseSet);
    }

    @Test
    void registerCourseToStudentReturnsStudentWithCourse() {
        long invalidId = 3;
        Exception thrown = assertThrows(EntityNotFoundException.class, () -> studentService.registerCourse(invalidId, course1));
        Assertions.assertEquals(thrown.getMessage(), "Student with ID: " + invalidId + " not found.");
    }
}
