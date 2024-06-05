package com.example.PathToGrade.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Curso {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private int qtdPeriodos;
}
