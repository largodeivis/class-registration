package com.academic.classregistration.controller;

import com.academic.classregistration.model.Student;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void createStudentReturnsStudent() throws Exception {
        Student student = new Student(0, "Freddy");
        String studentJson = "{\"name\": \"Freddy\"}";

        when(studentService.createStudent(Mockito.any(Student.class))).thenReturn(student);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/students")
                .accept(MediaType.APPLICATION_JSON).content(studentJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(student);

        JSONAssert.assertEquals(expected, response.getContentAsString(), false);

    }

    @Test
    void getStudentsReturnsAllStudents() throws Exception {
        Student student1 = new Student(0, "Freddy");
        Student student2 = new Student(1, "Daphne");
        Iterable<Student> studentIterable = Arrays.asList(student1, student2);
        when(studentService.getStudents()).thenReturn(studentIterable);

        this.mockMvc.perform(get("/students")).andDo(print())
                .andExpect(content().string(containsString("\"name\":\"Freddy\"")))
                .andExpect(content().string(containsString("\"name\":\"Daphne\"")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getStudentReturnsStudent() throws Exception {
        long studentId = 0;
        Student student1 = new Student(studentId, "Velma");
        when(studentService.getStudent(studentId)).thenReturn(student1);

        this.mockMvc.perform(get("/students/"+studentId)).andDo(print())
                .andExpect(content().string(containsString("\"name\":\"Velma\"")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getStudentInvalidIdThrowsException() throws Exception {
        long studentId = 99;
        String errorString = "Student with ID: " + studentId + " not found.";
        when(studentService.getStudent(studentId)).thenThrow(new EntityNotFoundException(errorString));

        this.mockMvc.perform(get("/students/"+studentId)).andDo(print())
                .andExpect(content().string(containsString(errorString)))
                .andExpect(status().is2xxSuccessful());
    }
}
