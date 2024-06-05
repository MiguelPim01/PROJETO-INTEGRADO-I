package com.example.PathToGrade.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Curso {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private Integer qtdPeriodos;

    @OneToMany(mappedBy = "curso")
    private List<Disciplina> disciplinas;

    public Curso(String nome, Integer qtdPeriodos) {
        this.nome = nome;
        this.qtdPeriodos = qtdPeriodos;
        this.disciplinas = new ArrayList<>();
    }

    public void addDisciplina(Disciplina disciplina) {
        disciplina.setCurso(this);
        this.disciplinas.add(disciplina);
    }
    
}
