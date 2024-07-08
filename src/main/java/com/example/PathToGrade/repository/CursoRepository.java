package com.example.PathToGrade.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.PathToGrade.domain.Curso;

/**
 * <i>Documentação da classe CursoRepository</i>
 * @author Miguel Vieira Machado Pim
 * @see com.example.PathToGrade.repository.CursoRepository
 * 
 * Repositório CRUD para armazenar objetos da classe Curso.
 */
public interface CursoRepository extends CrudRepository<Curso, Long> {
    
    /**
     * Busca um Curso no repositorio a partir do nome dele.
     * 
     * @param nome nome do Curso.
     * @return Retorna um tipo Optional de Curso.
     */
    Optional<Curso> findByNome(String nome);
}
