package com.academic.classregistration.service;

import com.academic.classregistration.jpa.StudentRepository;
import com.academic.classregistration.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student){
        studentRepository.save(student);
        logger.info("Successfully saved student into DB: " + student);
        return student;
    }

    public Student getStudent(Long id){
        Optional<Student> student = studentRepository.findById(id);
        if(student.isPresent()){
            logger.info("Retrieved student with ID: " + id);
            return student.get();
        } else {
            String errorString = "Student with ID: " + id + " not found.";
            logger.error(errorString);
            throw new EntityNotFoundException(errorString);
        }
    }

    public Iterable<Student> getStudents() {
        return studentRepository.findAll();
    }
}
