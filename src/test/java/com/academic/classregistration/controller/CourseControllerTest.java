package com.academic.classregistration.controller;

import com.academic.classregistration.model.Course;
import com.academic.classregistration.model.Professor;
import com.academic.classregistration.model.Student;
import com.academic.classregistration.service.CourseService;
import com.academic.classregistration.service.ProfessorService;
import com.academic.classregistration.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private ProfessorService professorService;

    @MockBean
    private StudentService studentService;

    @Test
    void createCourseReturnsCourse() throws Exception {
        Course course = new Course(0, "Software Testing", "SWT101");
        String courseJson = "{\"courseName\":\"Software Testing\",\"courseNumber\":\"SWT101\"}";

        when(courseService.createCourse(Mockito.any(Course.class))).thenReturn(course);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/courses")
                .accept(MediaType.APPLICATION_JSON).content(courseJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(course);

        JSONAssert.assertEquals(expected, response.getContentAsString(), false);
    }

    @Test
    void getCoursesReturnsAllCourses() throws Exception {
        Course course1 = new Course(0, "Mystery Solving", "MYS101");
        Course course2 = new Course(1, "Software Testing", "SWT101");
        Iterable<Course> courseIterable = Arrays.asList(course1, course2);
        when(courseService.getCourses()).thenReturn(courseIterable);

        this.mockMvc.perform(get("/courses")).andDo(print())
                .andExpect(content().string(containsString("\"courseNumber\":\"MYS101\"")))
                .andExpect(content().string(containsString("\"courseNumber\":\"SWT101\"")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getStudentReturnsStudent() throws Exception {
        long courseId = 0;
        Course course1 = new Course(0, "Mystery Solving", "MYS101");
        when(courseService.getCourse(courseId)).thenReturn(course1);

        this.mockMvc.perform(get("/courses/"+courseId)).andDo(print())
                .andExpect(content().string(containsString("\"courseName\":\"Mystery Solving\"")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getStudentInvalidIdThrowsException() throws Exception {
        long courseId = 99;
        String errorString = "Course with ID: " + courseId + " not found.";
        when(courseService.getCourse(courseId)).thenThrow(new EntityNotFoundException(errorString));

        this.mockMvc.perform(get("/courses/"+courseId)).andDo(print())
                .andExpect(content().string(containsString(errorString)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void assignProfessorReturnsCourseWithProfessor() throws Exception {
        long courseId = 0;
        long professorId = 0;
        Course course = new Course(courseId, "Software Testing", "SWT101");
        Professor professor = new Professor(professorId, "Scooby Doo");
        course.setProfessor(professor);

        when(professorService.getProfessor(anyLong())).thenReturn(professor);
        when(courseService.assignProfessor(anyLong(), anyLong())).thenReturn(course);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/courses/"+courseId+"/professor/"+professorId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(course);

        JSONAssert.assertEquals(expected, response.getContentAsString(), false);
    }

    @Test
    void registerStudentReturnsCourseWithStudent() throws Exception {
        long courseId = 0;
        long studentId = 0;
        Course course = new Course(courseId, "Software Testing", "SWT101");
        Student student = new Student(studentId, "Shaggy");
        course.registerStudent(student);

        when(courseService.getCourse(anyLong())).thenReturn(course);
        when(studentService.getStudent(anyLong())).thenReturn(student);
        when(courseService.studentCourseRegistration(Mockito.any(Course.class), anyLong())).thenReturn(course);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/courses/"+courseId+"/student/"+studentId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(course);

        JSONAssert.assertEquals(expected, response.getContentAsString(), false);
    }
}
