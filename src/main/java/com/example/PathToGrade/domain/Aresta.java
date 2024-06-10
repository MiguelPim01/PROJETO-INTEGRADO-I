package com.example.PathToGrade.domain;

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
public class Aresta {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String origemCodigo;
    private String destinoCodigo;

    @ManyToOne
    @JoinColumn(name = "origem_id")
    @JsonIgnore
    private Disciplina origem;

    @ManyToOne
    @JoinColumn(name = "destino_id")
    @JsonIgnore
    private Disciplina destino;

    public Aresta(Disciplina origem, Disciplina destino) {
        this.origem = origem;
        this.destino = destino;
        this.destinoCodigo = destino.getCodigo();
        this.origemCodigo = origem.getCodigo();
    }
}
