package com.example.PathToGrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.PathToGrade.domain.Curso;
import com.example.PathToGrade.service.CursoService;

@RestController
public class CursoController {
    
    @Autowired
    private CursoService cursoService;

    @PostMapping("/curso")
    public void postCurso(@RequestBody Curso curso) {
        cursoService.saveCurso(curso);
    }

    @GetMapping("/curso")
    public Iterable<Curso> getCursos() {
        return cursoService.getCursos();
    }
}
