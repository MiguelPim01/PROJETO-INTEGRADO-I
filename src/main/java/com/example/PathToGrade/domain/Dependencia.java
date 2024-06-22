package com.example.PathToGrade.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <i>Documentação da classe Dependencia</i>
 * @author Miguel Vieira Machado Pim
 * @see com.example.PathToGrade.domain.Dependencia
 * 
 * Classe para armazenar os códigos de duas disciplinas onde uma é pré-requisito da outra.
 * 
 * @version %I%, %G%
 */
@Getter
@Setter
@NoArgsConstructor
public class Dependencia {
    
    /**
     * Código da disciplina pré-requisito.
     */
    private String disciplinaA;

    /**
     * Código da disciplina.
     */
    private String disciplinaB;

    /**
     * Construtor.
     * 
     * @param disciplinaA disciplina pré-requisito. Não pode ser <code>null</code>.
     * @param disciplinaB disciplina. Não pode ser <code>null</code>.
     */
    public Dependencia(String disciplinaA, String disciplinaB) {
        this.disciplinaA = disciplinaA;
        this.disciplinaB = disciplinaB;
    }
}
