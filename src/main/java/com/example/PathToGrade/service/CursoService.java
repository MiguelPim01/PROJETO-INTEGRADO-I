package com.example.PathToGrade.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.PathToGrade.domain.Curso;
import com.example.PathToGrade.domain.Disciplina;
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

    public void saveDisciplinaFromCurso(Long cursoId, Disciplina disciplina) {
        Optional<Curso> cursoOp = cursoRepository.findById(cursoId);

        if (cursoOp.isPresent()) {
            Curso curso = cursoOp.get();

            // Faz a relação entre a Disciplina e o Curso
            curso.addDisciplina(disciplina);

            // Leva essa informação para o banco de dados
            cursoRepository.save(curso);
        }
        else {
            throw new RuntimeException("Curso não encontrado com id: " + cursoId);
        }
    }

    public List<Disciplina> getDisciplinasFromCurso(Long cursoId) {
        Optional<Curso> cursoOp = cursoRepository.findById(cursoId);

        if (cursoOp.isPresent()) {
            Curso curso = cursoOp.get();

            return curso.getDisciplinas();
        }
        else {
            throw new RuntimeException("Curso não encontrado com id: " + cursoId);
        }
    }

    public void addDependencia(Long cursoId, Long disciplinaA, Long disciplinaB) {
        Optional<Curso> cursoOp = cursoRepository.findById(cursoId);

        if (cursoOp.isPresent()) {
            Curso curso = cursoOp.get();

            curso.addPreRequisito(disciplinaA, disciplinaB);

            cursoRepository.save(curso);
        }
        else {
            throw new RuntimeException("Curso não encontrado com id: " + cursoId);
        }
    }
}
