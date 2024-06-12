package com.example.PathToGrade.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/curso/{cursoId}")
    public void putCurso(@PathVariable("cursoId") Long cursoId, @RequestBody Curso curso) {
        cursoService.modifyCurso(cursoId, curso);
    }

    @DeleteMapping("/curso/{cursoId}")
    public void deleteCurso(@PathVariable(name = "cursoId") Long cursoId) {
        cursoService.deleteCurso(cursoId);
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
        cursoService.saveDisciplinaInCurso(cursoId, disciplina);
    }

    @DeleteMapping("/curso/{cursoId}/disciplina/{disciplinaId}")
    public void deleteDisciplinaFromCurso(@PathVariable("cursoId") Long cId, @PathVariable("disciplinaId") Long dId) {
        cursoService.deleteDisciplinaFromCurso(cId, dId);
    }

    // Funcao para adicionar uma lista de disciplinas em um curso especifico
    @PostMapping("/curso/{cursoId}/disciplinas")
    public void postDisciplinaListInCurso(@PathVariable("cursoId") Long cursoId, @RequestBody List<Disciplina> disciplinas) {
        cursoService.saveDisciplinaListInCurso(cursoId, disciplinas);
    }

    /*
     *  Operações HTTP para adicionar um pré requisito a uma disciplina
     */
    @PostMapping("/curso/{cursoId}/dependencia")
    public void postDependencia(@PathVariable("cursoId") Long cursoId, @RequestBody Dependencia dependencia) {
        cursoService.addDependencia(cursoId, dependencia.getDisciplinaA(), dependencia.getDisciplinaB());
    }

    // Funcao para adicionar uma lista de arestas em um curso especifico
    @PostMapping("curso/{cursoId}/dependencias")
    public void postDependenciaListInCurso(@PathVariable("cursoId") Long cursoId, @RequestBody List<Dependencia> dependencias) {
        cursoService.addDependenciaListInCurso(cursoId, dependencias);
    }
}
