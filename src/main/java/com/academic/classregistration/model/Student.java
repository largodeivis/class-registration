package com.academic.classregistration.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotEmpty
    private String name;

    @ManyToMany
    private Set<Course> courses;

    Student(){
    }

    Student(String name){
        this.name = name;
    }

    public void registerCourse(Course course){
        this.courses.add(course);
    }
}
