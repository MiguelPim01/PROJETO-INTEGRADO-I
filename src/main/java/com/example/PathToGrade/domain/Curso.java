package com.example.PathToGrade.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;

import jakarta.persistence.CascadeType;
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

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void addPreRequisito(String disciplinaA, String disciplinaB) {
        Disciplina disciplina = null, preRequisito = null;

        for (Disciplina d : this.disciplinas) {
            if (d.getCodigo().equals(disciplinaA)) {
                preRequisito = d;
                break;
            }
        }

        if (preRequisito == null) {
            throw new RuntimeException("fatal error: Disciplina não encontrada, id: " + disciplinaA);
        }

        for (Disciplina d : this.disciplinas) {
            if (d.getCodigo().equals(disciplinaB)) {
                disciplina = d;
                break;
            }
        }

        if (disciplina == null) {
            throw new RuntimeException("fatal error: Disciplina não encontrada, id: " + disciplinaB);
        }

        disciplina.addArestaChegando(preRequisito);
    }

    public Set<Pair<Disciplina, Disciplina>> findPathToDisciplina(Long disciplinaId) {
        Set<Disciplina> visitados = new HashSet<>();
        Set<Pair<Disciplina, Disciplina>> dependencias = new HashSet<>();

        Disciplina inicio = null;

        for (Disciplina d : this.disciplinas) {
            if (d.getId().equals(disciplinaId)) {
                inicio = d;
                break;
            }
        }

        if (inicio == null) {
            throw new RuntimeException("fatal error: Disciplina não encontrada, id: " + disciplinaId);
        }

        visitados.add(inicio);

        this.dfs(dependencias, visitados, inicio);
        this.dfsTransp(dependencias, visitados, inicio);

        return dependencias;
    }

    public void dfs(Set<Pair<Disciplina, Disciplina>> dependencias, Set<Disciplina> visitados, Disciplina atual) {
        for (Aresta a : atual.getArestasSaindo()) {
            if (!visitados.contains(a.getDestino())) {
                visitados.add(a.getDestino());
                dependencias.add(new Pair<Disciplina,Disciplina>(atual, a.getDestino()));
                dfs(dependencias, visitados, a.getDestino());
            }
        }
    }

    public void dfsTransp(Set<Pair<Disciplina, Disciplina>> dependencias, Set<Disciplina> visitados, Disciplina atual) {
        for (Aresta a : atual.getArestasChegando()) {
            if (!visitados.contains(a.getOrigem())) {
                visitados.add(a.getOrigem());
                dependencias.add(new Pair<Disciplina,Disciplina>(a.getOrigem(), atual));
                dfsTransp(dependencias, visitados, a.getOrigem());
            }
        }
    }
    
}
