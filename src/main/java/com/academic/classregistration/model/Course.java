package com.academic.classregistration.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @NotBlank
    private String courseName;

    @NotBlank
    @Column(unique = true)
    private String courseNumber;

    @ManyToMany(mappedBy="courses")
    private Set<Student> students = new HashSet<>();

    @ManyToOne
    private Professor professor;

    Course() {
    }

    Course(String courseName, String courseNumber){
        this.courseName = courseName;
        this.courseNumber = courseNumber;
    }

    public void registerStudent(Student student){
        this.students.add(student);
    }

//    public void registerProfessor(Professor professor){
//        this.professor = professor;
//    }

    public boolean unregisterStudent(Student student){
        if (students.contains(student)) {
            this.students.remove(student);
            return true;
        } else {
            return false;
        }
    }

//    public void unregisterProfessor(Professor professor){
//        this.professor = null;
//    }





}
