package com.academic.classregistration.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String courseName;

    @ManyToOne
    private Professor professor;

    @ManyToMany
    private Set<Student> students;

    Course() {
    }

    Course(String courseName){
        this.courseName = courseName;
        this.professor = null;
        this.students = null;
    }

    public void registerStudent(Student student){
        this.students.add(student);
    }




}
