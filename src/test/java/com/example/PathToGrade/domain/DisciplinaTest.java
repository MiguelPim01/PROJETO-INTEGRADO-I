package com.example.PathToGrade.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DisciplinaTest {
    
    private Disciplina disciplina;

    @BeforeEach
    public void init() {
        disciplina = new Disciplina("d1", "COD0001", 60, 1);
    }

    @Test
    public void testModificacao() {
        Disciplina novaDisciplina = new Disciplina("d2", "COD0002", 90, 2);

        disciplina.modifyDisciplina(novaDisciplina);

        assertTrue((disciplina.getNome() == novaDisciplina.getNome()) &&
                   (disciplina.getCodigo() == novaDisciplina.getCodigo()) && 
                   (disciplina.getCargaHoraria() == novaDisciplina.getCargaHoraria()) &&
                   (disciplina.getPeriodo() == novaDisciplina.getPeriodo()));
    }
}
