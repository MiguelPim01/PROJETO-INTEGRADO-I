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
public class Disciplina {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private String codigo;
    private Integer cargaHoraria;
    private Integer periodo;
}
