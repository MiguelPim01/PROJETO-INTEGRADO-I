package com.example.PathToGrade.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.PathToGrade.exceptions.InvalidDependenciaException;
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


/**
 * <i>Documentação da classe Disciplina</i>
 * @author Miguel Vieira Machado Pim
 * @see com.example.PathToGrade.domain.Disciplina
 * 
 * A classe Disciplina é a abstração de um vértice de um grafo. Além de armazenar as informações de uma disciplina como,
 * id, nome, codigo, carga horária e periodo, ela também armazena a arestas que vão dela para uma outra disciplina,
 * e as aresta que chegam de uma disciplina até ela.
 * 
 * @version %I%, %G%
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Disciplina {

    /**
     * Id utilizado pelo banco de dados.
     */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Nome da disciplina.
     */
    private String nome;

    /**
     * Código da disciplina.
     */
    private String codigo;

    /**
     * Carga horária da disciplina.
     */
    private Integer cargaHoraria;

    /**
     * Período da disciplina.
     */
    private Integer periodo;

    /**
     * Arestas saindo da disciplina.
     */
    @OneToMany(mappedBy = "origem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Aresta> arestasSaindo;

    /**
     * Arestas chegando na disciplina.
     */
    @OneToMany(mappedBy = "destino", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Aresta> arestasChegando;

    /**
     * Curso a que a disciplina pertence.
     */
    @ManyToOne
    @JoinColumn(name = "curso_id")
    @JsonIgnore
    private Curso curso;

    /**
     * Construtor.
     * 
     * @param nome nome da disciplina. Não pode ser <code>null</code>.
     * @param codigo codigo da disciplina. Não pode ser <code>null</code>.
     * @param cargaHoraria carga horária da disciplina. Não pode ser <code>null</code>.
     * @param periodo período da disciplina. Não pode ser <code>null</code>.
     */
    public Disciplina(String nome, String codigo, Integer cargaHoraria, Integer periodo) {
        this.nome = nome;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
        this.periodo = periodo;
        this.arestasChegando = new ArrayList<>();
        this.arestasSaindo = new ArrayList<>();
    }

    /**
     * Adiciona uma aresta chegando a disciplina.
     * <p>
     * Note: Essa função adiciona uma aresta chegando na disciplina e também adiciona uma aresta saindo do pré-requisito.
     * 
     * @param preRequisito disciplina pré-requisito. Não pode ser <code>null</code>.
     * @throws InvalidDependenciaException Dependencia invalida.
     */
    public void addArestaChegando(Disciplina preRequisito) throws InvalidDependenciaException {
        Aresta a = new Aresta(preRequisito, this);

        for (Aresta ar : this.arestasChegando) {
            if (ar.getOrigemCodigo().equals(a.getOrigemCodigo()) && ar.getDestinoCodigo().equals(a.getDestinoCodigo())) {
                throw new InvalidDependenciaException("A dependencia já existe neste curso.");
            }
        }

        this.arestasChegando.add(a);

        preRequisito.addArestaSaindo(a);
    }

    /**
     * Adiciona uma aresta saindo da disciplina.
     * <p>
     * Note: O método que deve ser usado é o {@link #addArestaChegando(Disciplina)} pois este já usa essa função.
     * 
     * @param a aresta a ser adicionada
     */
    public void addArestaSaindo(Aresta a) {
        this.arestasSaindo.add(a);
    }

    /**
     * Modifica os atributos de uma disciplina.
     * 
     * @param novaDisciplina Não pode ser <code>null</code>.
     */
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
