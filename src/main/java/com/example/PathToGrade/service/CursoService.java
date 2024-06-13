package com.example.PathToGrade.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.PathToGrade.domain.Curso;
import com.example.PathToGrade.domain.Dependencia;
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

    public void modifyCurso(Long cursoId, Curso curso) {
        Optional<Curso> cursoOp = cursoRepository.findById(cursoId);

        if (cursoOp.isPresent()) {
            Curso c = cursoOp.get();

            c.setNome(curso.getNome());
            c.setQtdPeriodos(curso.getQtdPeriodos());

            cursoRepository.save(c);
        }
        else {
            throw new RuntimeException("Curso não encontrado com id: " + cursoId);
        }
    }

    public void deleteCurso(Long id)  {
        cursoRepository.deleteById(id);
    }

    public void saveDisciplinaInCurso(Long cursoId, Disciplina disciplina) {
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

    public void modifyDisciplinaFromCurso(Long cursoId, Long disciplinaId, Disciplina disciplina) {
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

            for (Disciplina d : curso.getDisciplinas()) {
                if (d.getId().equals(disciplinaId)) {
                    d.modifyDisciplina(disciplina);
                    break;
                }
            }

            cursoRepository.save(curso);
        }
        else {
            throw new RuntimeException("Curso não encontrado com id: " + cursoId);
        }
    }

    public void deleteDisciplinaFromCurso(Long cursoId, Long disciplinaId) {
        Optional<Curso> cursoOp = cursoRepository.findById(cursoId);
        boolean flag = false;

        if (cursoOp.isPresent()) {
            Curso curso = cursoOp.get();

            for (Disciplina d : curso.getDisciplinas()) {
                if (d.getId().equals(disciplinaId)) {
                    curso.getDisciplinas().remove(d);
                    flag = true;
                    break;
                }
            }

            if (flag) {
                cursoRepository.save(curso);
            }
            else {
                throw new RuntimeException("Error: Disciplina não existe!");
            }
        }
        else {
            throw new RuntimeException("Curso não encontrado com id: " + cursoId);
        }
    }

    public void saveDisciplinaListInCurso(Long cursoId, List<Disciplina> disciplinas) {
        
        for (Disciplina d : disciplinas) {
            this.saveDisciplinaInCurso(cursoId, d);    
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

    public void addDependenciaListInCurso(Long cursoId, List<Dependencia> dependencias) {
        for (Dependencia d : dependencias) {
            this.addDependencia(cursoId, d.getDisciplinaA(), d.getDisciplinaB());
        }
    }

    public Set<Pair<Disciplina, Disciplina>> getPathFromDisciplina(Long cId, Long dId) {
        Optional<Curso> cursoOp = cursoRepository.findById(cId);

        if (cursoOp.isPresent()) {
            Curso curso = cursoOp.get();

            return curso.findPathToDisciplina(dId);
        }
        else {
            throw new RuntimeException("Curso não encontrado com id: " + cId);
        }
    }
}
