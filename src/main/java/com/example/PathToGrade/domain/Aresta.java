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

/**
 * <i>Documentação da classe Aresta</i>
 * @author Miguel Vieira Machado Pim
 * @see com.example.PathToGrade.domain.Aresta
 * 
 * Classe para representar uma aresta em um Curso.
 * <p>
 * Contém a referência para duas disciplinas e os seus respectivos códigos, onde uma Disciplina é a origem da Aresta
 * e a outra é o destino da Aresta.
 * 
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Aresta {
    
    /**
     * Id da aresta no banco de dados.
     */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Código da disciplina origem.
     */
    private String origemCodigo;

    /**
     * Código da disciplina destino.
     */
    private String destinoCodigo;

    /**
     * Referência para a Disciplina origem.
     */
    @ManyToOne
    @JoinColumn(name = "origem_id")
    @JsonIgnore
    private Disciplina origem;

    /**
     * Código para a Disciplina destino.
     */
    @ManyToOne
    @JoinColumn(name = "destino_id")
    @JsonIgnore
    private Disciplina destino;

    /**
     * Construtor.
     * 
     * @param origem disciplina origem. Não pode ser <code>null</code>.
     * @param destino diciplina destino. Não pode ser <code>null</code>.
     */
    public Aresta(Disciplina origem, Disciplina destino) {
        this.origem = origem;
        this.destino = destino;
        this.destinoCodigo = destino.getCodigo();
        this.origemCodigo = origem.getCodigo();
    }
}
