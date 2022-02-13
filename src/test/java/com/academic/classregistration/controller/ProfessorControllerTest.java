package com.academic.classregistration.controller;

import com.academic.classregistration.model.Professor;
import com.academic.classregistration.service.ProfessorService;
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

@WebMvcTest(ProfessorController.class)
public class ProfessorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessorService professorService;

    @Test
    void createProfessorReturnsProfessor() throws Exception {
        Professor professor = new Professor(0, "Scrappy Doo");
        String professorJson = "{\"name\": \"Scrappy Doo\"}";

        when(professorService.createProfessor(Mockito.any(Professor.class))).thenReturn(professor);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/professors")
                .accept(MediaType.APPLICATION_JSON).content(professorJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(professor);

        JSONAssert.assertEquals(expected, response.getContentAsString(), false);

    }

    @Test
    void getProfessorsReturnsAllProfessors() throws Exception {
        Professor professor1 = new Professor(0, "Scooby Doo");
        Professor professor2 = new Professor(1, "Scrappy Doo");
        Iterable<Professor> professorIterable = Arrays.asList(professor1, professor2);
        when(professorService.getProfessors()).thenReturn(professorIterable);

        this.mockMvc.perform(get("/professors")).andDo(print())
                .andExpect(content().string(containsString("\"name\":\"Scooby Doo\"")))
                .andExpect(content().string(containsString("\"name\":\"Scrappy Doo\"")))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    void getProfessorReturnsProfessor() throws Exception {
        long professorId = 0;
        Professor professor1 = new Professor(professorId, "Scooby Doo");
        when(professorService.getProfessor(professorId)).thenReturn(professor1);

        this.mockMvc.perform(get("/professors/"+professorId)).andDo(print())
                .andExpect(content().string(containsString("\"name\":\"Scooby Doo\"")))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    void getProfessorInvalidIdThrowsException() throws Exception {
        long professorId = 99;
        String errorString = "Professor with ID: " + professorId + " not found.";
        when(professorService.getProfessor(professorId)).thenThrow(new EntityNotFoundException(errorString));

        this.mockMvc.perform(get("/professors/"+professorId)).andDo(print())
                .andExpect(content().string(containsString(errorString)))
                .andExpect(status().is2xxSuccessful());
    }
}
