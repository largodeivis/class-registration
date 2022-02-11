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
@Table(name="professor")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @NotBlank
    private String name;

    @OneToMany
    //@JoinTable(name="professor_courses", joinColumns = @JoinColumn(name="professor_id"), inverseJoinColumns = @JoinColumn(name="course_id"))
    private Set<Course> courses = new HashSet<>();

    Professor(){
    }

    Professor(String name){
        this.name = name;
    }
}
