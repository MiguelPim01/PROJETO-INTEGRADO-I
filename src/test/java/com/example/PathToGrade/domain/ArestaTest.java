package com.example.PathToGrade.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArestaTest {
    
    private Aresta aresta;

    @BeforeEach
    public void init() {
        Disciplina d1 = new Disciplina("d1", "COD0001", 60, 1);
        Disciplina d2 = new Disciplina("d2", "COD0002", 60, 1);
        aresta = new Aresta(d1, d2);
    }

    @Test
    public void testVerticesIguais() {
        assertFalse(aresta.getDestinoCodigo().equals(aresta.getOrigemCodigo()));
    }
}
