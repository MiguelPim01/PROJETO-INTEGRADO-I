package com.example.PathToGrade.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.PathToGrade.domain.Curso;
import com.example.PathToGrade.domain.Dependencia;
import com.example.PathToGrade.domain.Disciplina;
import com.example.PathToGrade.service.CursoService;

@RestController
public class CursoController {
    
    @Autowired
    private CursoService cursoService;

    /*
     *  Operações HTTP para serem feitas com Curso
     */
    @GetMapping("/curso")
    public Iterable<Curso> getCursos() {
        return cursoService.getCursos();
    }

    @PostMapping("/curso")
    public void postCurso(@RequestBody Curso curso) {
        cursoService.saveCurso(curso);
    }

    /*
     *  Operações HTTP para serem feitas com Disciplina
     */
    @GetMapping("/curso/{cursoId}/disciplina")
    public List<Disciplina> getDisciplinasFromCurso(@PathVariable("cursoId") Long cursoId) {
        return cursoService.getDisciplinasFromCurso(cursoId);
    }

    @PostMapping("/curso/{cursoId}/disciplina")
    public void postDisciplinaInCurso(@PathVariable("cursoId") Long cursoId, @RequestBody Disciplina disciplina) {
        cursoService.saveDisciplinaFromCurso(cursoId, disciplina);
    }

    /*
     *  Operações HTTP para adicionar um pré requisito a uma disciplina
     */
    @PostMapping("/curso/{cursoId}/dependencia")
    public void postDependencia(@PathVariable("cursoId") Long cursoId, @RequestBody Dependencia dependencia) {
        cursoService.addDependencia(cursoId, dependencia.getDisciplinaA(), dependencia.getDisciplinaB());
    }
}
