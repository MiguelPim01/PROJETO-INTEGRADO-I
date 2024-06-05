package com.example.PathToGrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.PathToGrade.domain.Curso;
import com.example.PathToGrade.repository.CursoRepository;

@Service
public class CursoService {
    
    @Autowired
    private CursoRepository cursoRepository;

    public void saveCurso(Curso curso) {
        cursoRepository.save(curso);
    }

    public Iterable<Curso> getCursos() {
        return cursoRepository.findAll();
    }
}
