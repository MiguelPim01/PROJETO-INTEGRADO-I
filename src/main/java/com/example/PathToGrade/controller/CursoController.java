package com.example.PathToGrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.PathToGrade.service.CursoService;

@RestController
public class CursoController {
    
    @Autowired
    private CursoService cursoService;

    
}
