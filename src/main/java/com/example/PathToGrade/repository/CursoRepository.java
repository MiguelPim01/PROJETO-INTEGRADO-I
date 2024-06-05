package com.example.PathToGrade.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.PathToGrade.domain.Curso;

public interface CursoRepository extends CrudRepository<Curso, Long> {
    
}
