package com.academic.classregistration.controller;

import com.academic.classregistration.model.Student;
import com.academic.classregistration.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("students")
    public ResponseEntity<Student> createStudent(@RequestBody Student student){
        Student newStudent = studentService.createStudent(student);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    @GetMapping("students")
    public ResponseEntity<Iterable<Student>> getStudents() {
        Iterable<Student> students = studentService.getStudents();
        System.out.print(students);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("students/{id}")
    public ResponseEntity<Object> getStudent(@PathVariable Long id){
        try {
            Student student = studentService.getStudent(id);
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch (EntityNotFoundException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
        }
    }
}
