package com.example.PathToGrade.dto;

import com.example.PathToGrade.domain.Disciplina;
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
public class DisciplinaDTO {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private Long preRequisitoId;
    private String nome;

    @ManyToOne
    @JoinColumn(name = "disciplina_id")
    @JsonIgnore
    private Disciplina disciplina;

    public DisciplinaDTO(Disciplina disciplina) {
        this.preRequisitoId = disciplina.getId();
        this.nome = disciplina.getNome();
        this.disciplina = disciplina;
    }
}
