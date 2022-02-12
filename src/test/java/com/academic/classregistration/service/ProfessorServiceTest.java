package com.academic.classregistration.service;

import com.academic.classregistration.jpa.ProfessorRepository;
import com.academic.classregistration.model.Professor;
import com.academic.classregistration.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTest {
    @Mock
    private ProfessorRepository professorRepository;

    private ProfessorService professorService;

    Professor professor1 = new Professor(1, "Scooby Doo");
    Professor professor2 = new Professor(2, "Shaggy");

    List<Professor> professorList;

    @BeforeEach
    public void setUp(){
        professorService = new ProfessorService(professorRepository);
        professorList = new ArrayList<>();
        professorList.add(professor1);
        professorList.add(professor2);
    }

    @AfterEach
    public void tearDown(){
        professorService = null;
        professorList = null;
        professor1 = null;
        professor2 = null;
    }

    @Test
    void getProfessorReturnsProfessor(){
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor1));
        Professor professor = professorService.getProfessor(1L);
        assertEquals(professor1.getName(), professor.getName());
    }

    @Test
    void getProfessorInvalidIdThrowsEntityNotFoundException(){
        long invalidId = 3;
        Exception thrown = assertThrows(EntityNotFoundException.class, () -> professorService.getProfessor(invalidId));
        assertEquals(thrown.getMessage(), "Professor with ID: " + invalidId + " not found.");
    }

    @Test
    void getProfessorsReturnsAllProfessors(){
        when(professorRepository.findAll()).thenReturn(professorList);
        Iterable<Professor> professors = professorService.getProfessors();
        List<Professor> iterableList = new ArrayList<>();
        professors.forEach(iterableList::add);
        assertEquals(professorList, iterableList);
    }
}
