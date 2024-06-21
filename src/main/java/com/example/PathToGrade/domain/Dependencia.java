package com.example.PathToGrade.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Dependencia {
    
    private String disciplinaA;
    private String disciplinaB;

    public Dependencia(String disciplinaA, String disciplinaB) {
        this.disciplinaA = disciplinaA;
        this.disciplinaB = disciplinaB;
    }
}
