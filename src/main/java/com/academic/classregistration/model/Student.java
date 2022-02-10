package com.academic.classregistration.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private UUID id;

    @NotEmpty
    private String name;

    @ManyToMany
    @JoinTable(name="student_courses", joinColumns = @JoinColumn(name="student_id"), inverseJoinColumns = @JoinColumn(name="course_id"))
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<CourseRegistration> registrations = new HashSet<>();

    Student(){
    }

    Student(String name){
        this.name = name;
    }

    public void registerCourse(Course course){
        this.courses.add(course);
    }
}
