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
        if (curso.getNome() == null || !(curso.getQtdPeriodos() > 0 && curso.getQtdPeriodos() <= 12)) {
            throw new RuntimeException("Error: Curso Inválido!");
        }

        Iterable<Curso> cursos = cursoRepository.findAll();

        for (Curso c : cursos) {
            if (c.getNome().equals(curso.getNome()) && c.getQtdPeriodos() == curso.getQtdPeriodos()) {
                throw new RuntimeException("Error: Curso já existe!");
            }
        }

        cursoRepository.save(curso);
    }

    public Iterable<Curso> getCursos() {
        return cursoRepository.findAll();
    }

    public void deleteCurso(Long id)  {
        cursoRepository.deleteById(id);
    }

    public void saveDisciplinaFromCurso(Long cursoId, Disciplina disciplina) {
        Optional<Curso> cursoOp = cursoRepository.findById(cursoId);

        if (cursoOp.isPresent()) {
            Curso curso = cursoOp.get();

            // Verifica se a disciplina é invalida
            if (disciplina.getCodigo() == null || 
            disciplina.getNome() == null || 
            !(disciplina.getPeriodo() > 0 && disciplina.getPeriodo() <= curso.getQtdPeriodos()) ||
            disciplina.getCargaHoraria() == null) {
                throw new RuntimeException("Error: Disciplina Inválida!");
            }

            // Verifica se a disciplina ja existe no curso
            for (Disciplina d : curso.getDisciplinas()) {
                if (d.getCodigo().equals(disciplina.getCodigo())) {
                    throw new RuntimeException("Error: Disciplina já existe!");
                }
            }

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

    public void addDependencia(Long cursoId, String disciplinaA, String disciplinaB) {
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
