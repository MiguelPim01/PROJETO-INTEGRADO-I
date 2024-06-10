package com.example.PathToGrade.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Disciplina {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private String codigo;
    private Integer cargaHoraria;
    private Integer periodo;

    private List<String> dependencias;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    @JsonIgnore
    private Curso curso;

    public Disciplina(String nome, String codigo, Integer cargaHoraria, Integer periodo) {
        this.nome = nome;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
        this.periodo = periodo;
    }

    public void addPreRequisito(Disciplina preRequisito) {
        if (this.dependencias == null) {
            this.dependencias = new ArrayList<>();
        }

        this.dependencias.add(preRequisito.codigo);
    }
}
