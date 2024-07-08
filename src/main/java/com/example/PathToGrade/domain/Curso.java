package com.example.PathToGrade.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;

import com.example.PathToGrade.exceptions.InvalidDependenciaException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <i>Documentação da classe Curso</i>
 * @author Miguel Vieira Machado Pim
 * @see com.example.PathToGrade.domain.Curso
 * 
 * A classe curso é a abstração de um grafo, ela armazena objetos da classe Disciplina, que são os vértices.
 * Essas disciplinas contém objetos da classe Aresta que conectam duas disciplinas diferentes em uma relação direcional.
 * <p>
 * Com isso temos todos os elementos de um grafo a partir da classe Curso.
 * 
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Curso {
    
    /**
     * Id utilizado pelo banco de dados.
     */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Nome do curso.
     */
    private String nome;

    /**
     * Quantidade total de períodos que o curso tem.
     */
    private Integer qtdPeriodos;

    /**
     * Lista de todas as matérias que o curso oferece.
     */
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disciplina> disciplinas;

    /**
     * Construtor.
     * 
     * @param nome nome do curso a ser criado. Não pode ser <code>null</code>
     * @param qtdPeriodos quantidade de períodos do curso
     */
    public Curso(String nome, Integer qtdPeriodos) {
        this.nome = nome;
        this.qtdPeriodos = qtdPeriodos;
        this.disciplinas = new ArrayList<>();
    }

    /**
     * Retorna uma Disciplina de um Curso a partir do seu código
     * 
     * @param codigo codigo da Disciplina
     * @return Disciplina
     * @throws EntityNotFoundException Não encontrou a Disciplina.
     */
    public Disciplina getDisciplinaByCodigo(String codigo) throws EntityNotFoundException {
        for (Disciplina d : this.disciplinas) {
            if (d.getCodigo().equals(codigo)) {
                return d;
            }
        }

        throw new EntityNotFoundException("Disciplina não encontrada com o código: " + codigo);
    }

    /**
     * Adiciona uma disciplina ao curso.
     * 
     * @param disciplina referência para a disciplina que será adicionada. Não pode ser <code>null</code>.
     */
    public void addDisciplina(Disciplina disciplina) {
        disciplina.setCurso(this);
        this.disciplinas.add(disciplina);
    }

    /**
     * Adiciona um pré-requisito ao curso, ou seja, uma aresta.
     * 
     * @param disciplinaA disciplina pré-requisito. Não pode ser <code>null</code>.
     * @param disciplinaB disciplina. Não pode ser <code>null</code>.
     * @throws EntityNotFoundException Não encontrou a Disciplina.
     * @throws InvalidDependenciaException Disciplina inválida.
     */
    public void addPreRequisito(String disciplinaA, String disciplinaB) throws EntityNotFoundException, InvalidDependenciaException {
        Disciplina disciplina = null, preRequisito = null;

        for (Disciplina d : this.disciplinas) {
            if (d.getCodigo().equals(disciplinaA)) {
                preRequisito = d;
                break;
            }
        }

        if (preRequisito == null) {
            throw new EntityNotFoundException("Disciplina não encontrada com id: " + disciplinaA);
        }

        for (Disciplina d : this.disciplinas) {
            if (d.getCodigo().equals(disciplinaB)) {
                disciplina = d;
                break;
            }
        }

        if (disciplina == null) {
            throw new EntityNotFoundException("Disciplina não encontrada com id: " + disciplinaB);
        }

        disciplina.addArestaChegando(preRequisito);
    }

    /**
     * A partir de uma disciplina específica, o método vai fazer um dfs no grafo, tanto no sentido normal das arestas
     * quanto no sentido contrário delas, para achar todas as outras disciplinas do curso que é possível chegar a partir dela.
     * 
     * @param disciplinaId id da disciplina em que o dfs irá iniciar.
     * @return Um conjunto com todos os pares de disciplinas encontrado pelo {@link #dfs(Set, Set, Disciplina)} e {@link #dfsTransp(Set, Set, Disciplina)}.
     * @throws EntityNotFoundException Lançada se não encontrar alguma das Disciplinas.
     */
    public Set<Pair<Disciplina, Disciplina>> findPathToDisciplina(Long disciplinaId) throws EntityNotFoundException {
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
            throw new EntityNotFoundException("Disciplina não encontrada com id: " + disciplinaId);
        }

        visitados.add(inicio);

        this.dfs(dependencias, visitados, inicio);
        this.dfsTransp(dependencias, visitados, inicio);

        return dependencias;
    }

    /**
     * Realiza o algoritmo de dfs no sentido normal das arestas.
     * 
     * @param dependencias conjunto de pares de disciplinas.
     * @param visitados conjunto de disciplinas que já foram visitadas.
     * @param atual vértice que o algoritmo irá visitar.
     */
    public void dfs(Set<Pair<Disciplina, Disciplina>> dependencias, Set<Disciplina> visitados, Disciplina atual) {
        for (Aresta a : atual.getArestasSaindo()) {
            if (!visitados.contains(a.getDestino())) {
                visitados.add(a.getDestino());
                dependencias.add(new Pair<Disciplina,Disciplina>(atual, a.getDestino()));
                dfs(dependencias, visitados, a.getDestino());
            }
        }
    }

    /**
     * Realiza o algoritmo de dfs no sentido inverso das arestas.
     * 
     * @param dependencias conjunto de pares de disciplinas.
     * @param visitados conjunto de disciplinas que já foram visitadas.
     * @param atual vértice que o algoritmo irá visitar.
     */
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
