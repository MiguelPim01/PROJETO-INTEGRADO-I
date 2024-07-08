package com.example.PathToGrade.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DependenciaTest {
    
    private Dependencia dependencia;

    @BeforeEach
    public void init() {
        dependencia = new Dependencia("COD0001", "COD0002");
    }

    @Test
    public void testCodigosIguais() {
        assertFalse(dependencia.getDisciplinaA().equals(dependencia.getDisciplinaB()));
    }

}
