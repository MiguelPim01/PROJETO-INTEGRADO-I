package com.example.PathToGrade.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.PathToGrade.dto.DisciplinaDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DisciplinaDTO> dependencias;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    @JsonIgnore
    private Curso curso;

    public Disciplina(String nome, String codigo, Integer cargaHoraria, Integer periodo) {
        this.nome = nome;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
        this.periodo = periodo;
        this.dependencias = new ArrayList<>();
    }

    public void addPreRequisito(Disciplina preRequisito) {
        DisciplinaDTO disciplinaDTO = new DisciplinaDTO(preRequisito);

        this.dependencias.add(disciplinaDTO);
    } 
}
