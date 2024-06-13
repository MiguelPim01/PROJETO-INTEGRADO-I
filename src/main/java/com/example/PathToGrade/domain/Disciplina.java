package com.example.PathToGrade.domain;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "origem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Aresta> arestasSaindo;

    @OneToMany(mappedBy = "destino", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Aresta> arestasChegando;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    @JsonIgnore
    private Curso curso;

    public Disciplina(String nome, String codigo, Integer cargaHoraria, Integer periodo) {
        this.nome = nome;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
        this.periodo = periodo;
        this.arestasChegando = new ArrayList<>();
        this.arestasSaindo = new ArrayList<>();
    }

    public void addArestaChegando(Disciplina preRequisito) {
        Aresta a = new Aresta(preRequisito, this);

        for (Aresta ar : this.arestasChegando) {
            if (ar.getOrigemCodigo().equals(a.getOrigemCodigo()) && ar.getDestinoCodigo().equals(a.getDestinoCodigo())) {
                throw new RuntimeException("fatal error: A dependencia já existe neste curso.");
            }
        }

        this.arestasChegando.add(a);

        preRequisito.addArestaSaindo(a);
    }

    public void addArestaSaindo(Aresta a) {
        this.arestasSaindo.add(a);
    }

    public void modifyDisciplina(Disciplina novaDisciplina) {
        this.nome = novaDisciplina.nome;
        this.codigo = novaDisciplina.codigo;
        this.cargaHoraria = novaDisciplina.cargaHoraria;
        this.periodo = novaDisciplina.periodo;

        for (Aresta a : this.getArestasChegando()) {
            a.setDestinoCodigo(this.codigo);
        }

        for (Aresta a : this.getArestasSaindo()) {
            a.setOrigemCodigo(this.codigo);
        }
    }
}
