package com.example.PathToGrade.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CursoTest {
    
    private Curso curso;

    @BeforeEach
    public void init() {
        curso  = new Curso("Curso teste", 10);
    }

    @Test
    public void testRelacaoCursoDisciplina() {
        Disciplina d = new Disciplina("d1", "COD0001", 60, 1);

        curso.addDisciplina(d);

        assertTrue(d.getCurso() == curso);
    }
}
