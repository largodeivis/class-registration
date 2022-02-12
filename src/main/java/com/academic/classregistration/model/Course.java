package com.academic.classregistration.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="course")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Course implements Serializable {

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

    public Course(String courseName, String courseNumber){
        this.courseName = courseName;
        this.courseNumber = courseNumber;
    }

    public Course(long id, String courseName, String courseNumber){
        this.id = id;
        this.courseName = courseName;
        this.courseNumber = courseNumber;
    }

    public void registerStudent(Student student){
        this.students.add(student);
    }

    public Course unregisterStudent(Student student){
        if (this.students.contains(student)) {
            this.students.remove(student);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id && courseNumber.equals(course.courseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseNumber);
    }
}
