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
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotBlank
    private String name;

    @OneToMany
    private Set<Course> courses;

    Professor(){
    }

    Professor(String name){
        this.name = name;
        this.courses = null;
    }

    public void registerCourse(Course course){
        this.courses.add(course);
    }
}
