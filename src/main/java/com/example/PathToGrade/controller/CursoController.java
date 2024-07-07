package com.example.PathToGrade.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.PathToGrade.exceptions.InvalidCursoException;
import com.example.PathToGrade.exceptions.InvalidDependenciaException;
import com.example.PathToGrade.exceptions.InvalidDisciplinaException;
import com.example.PathToGrade.service.CursoService;

import jakarta.persistence.EntityNotFoundException;

/**
 * <i>Documentação da classe CursoController</i>
 * @author Miguel Vieira Machado Pim
 * @see com.example.PathToGrade.controller.CursoController
 * 
 * Uma RestController para prover as operações HTTP necessárias para o funcionamento da API.
 * <p>
 * Os endpoints providos por essa controladora são:
 * <ul>
 * <li> GET "/curso"
 * <li> GET "/curso/{cursoId}/disciplina"
 * <li> GET "/curso/{cursoId}/path/{disciplinaId}"
 * <li> POST "/curso"
 * <li> POST "/curso/{cursoId}/disciplina"
 * <li> POST "/curso/{cursoId}/disciplinas"
 * <li> POST "/curso/{cursoId}/preRequisito"
 * <li> POST "/curso/{cursoId}/preRequisitos"
 * <li> PUT "/curso/{cursoId}"
 * <li> PUT "/curso/{cursoId}/disciplina/{disciplinaId}"
 * <li> DELETE "/curso/{cursoId}"
 * <li> DELETE "/curso/{cursoId}/disciplina/{disciplinaId}"
 * </ul>
 * Esses endpoints permitem a inclusão de cursos, disciplinas e pré-requisitos no banco de dados, assim como a deleção e modificação deles.
 * 
 * @version %I%, %G%
 */
@RestController
public class CursoController {
    
    /**
     * Variável para prover as funcionalidades de cursoService para a controladora.
     */
    @Autowired
    private CursoService cursoService;

    /**
     * Retorna todos os cursos do banco de dados.
     * 
     * @return Lista de Cursos.
     */
    @GetMapping("/curso")
    public ResponseEntity<Iterable<Curso>> getCursos() {
        return ResponseEntity.ok(cursoService.getCursos());
    }

    /**
     * Insere um Curso no banco de dados.
     * 
     * @param curso Curso passado no json.
     */
    @PostMapping("/curso")
    public ResponseEntity<Object> postCurso(@RequestBody Curso curso) {
        try {
            Long id = cursoService.saveCurso(curso);

            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        }
        catch (InvalidCursoException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Curso");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Retorna um curso a partir de seu id.
     * 
     * @param id id do Curso.
     * @return Curso.
     */
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<Object> getCursoById(@PathVariable("cursoId") Long id) {
        try {
            Curso curso = cursoService.getCursoById(id);

            return ResponseEntity.ok(curso);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Modifica um Curso no banco de dados.
     * 
     * @param cursoId id do Curso.
     * @param curso novo Curso contendo as informações a serem modificadas.
     */
    @PutMapping("/curso/{cursoId}")
    public ResponseEntity<Object> putCurso(@PathVariable("cursoId") Long cursoId, @RequestBody Curso curso) {
        try {
            cursoService.modifyCurso(cursoId, curso);

            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        catch (InvalidCursoException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Deleta um Curso do banco de dados.
     * 
     * @param cursoId id do Curso.
     */
    @DeleteMapping("/curso/{cursoId}")
    public ResponseEntity<Object> deleteCurso(@PathVariable(name = "cursoId") Long cursoId) {
        try {
            cursoService.deleteCurso(cursoId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Retorna todas as disciplinas de um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @return Lista de Disciplinas.
     */
    @GetMapping("/curso/{cursoId}/disciplina")
    public ResponseEntity<Object> getDisciplinasFromCurso(@PathVariable("cursoId") Long cursoId) {
        try {
            return ResponseEntity.ok(cursoService.getDisciplinasFromCurso(cursoId));
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Insere uma Disciplina em um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @param disciplina Disciplina a ser inserida.
     */
    @PostMapping("/curso/{cursoId}/disciplina")
    public ResponseEntity<Object> postDisciplinaInCurso(@PathVariable("cursoId") Long cursoId, @RequestBody Disciplina disciplina) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.saveDisciplinaInCurso(cursoId, disciplina));
        }
        catch (InvalidDisciplinaException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Retorna uma disciplina de um curso especifico.
     * 
     * @param cId id do Curso.
     * @param dId id da Disciplina.
     * @return retorna uma ResponseEntity com HttpStatus OK se encontrar e HttpStatus NOT FOUND caso não encontre
     */
    @GetMapping("/curso/{cursoId}/disciplina/{disciplinaId}")
    public ResponseEntity<Object> getDisciplinasByIdFromCurso(@PathVariable("cursoId") Long cId, @PathVariable("disciplinaId") Long dId) {
        try {
            Disciplina disciplina = cursoService.getDisciplinaById(cId, dId);

            return ResponseEntity.ok(disciplina);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> erroResponse = new HashMap<>();
            erroResponse.put("error", "Not Found");
            erroResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroResponse);
        }
    }

    /**
     * Modifica uma Disciplina de um Curso específico.
     * 
     * @param cId id do Curso.
     * @param dId id da Disciplina.
     * @param disciplina nova Disciplina contendo as informações a serem modificadas.
     */
    @PutMapping("/curso/{cursoId}/disciplina/{disciplinaId}")
    public ResponseEntity<Object> putDisciplinaFromCurso(@PathVariable("cursoId") Long cId, @PathVariable("disciplinaId") Long dId, @RequestBody Disciplina disciplina) {
        try {
            cursoService.modifyDisciplinaFromCurso(cId, dId, disciplina);

            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (InvalidDisciplinaException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Deleta uma Disciplina de um Curso específico.
     * 
     * @param cId id do Curso.
     * @param dId id da Disciplina.
     */
    @DeleteMapping("/curso/{cursoId}/disciplina/{disciplinaId}")
    public ResponseEntity<Object> deleteDisciplinaFromCurso(@PathVariable("cursoId") Long cId, @PathVariable("disciplinaId") Long dId) {
        try {
            cursoService.deleteDisciplinaFromCurso(cId, dId);

            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Insere uma lista de Disciplinas em um Curso.  
     * Função muito útil para a inserção mais rápida das disciplinas de um curso.
     * 
     * @param cursoId id do Curso.
     * @param disciplinas lista de Disciplinas a ser inserida.
     */
    @PostMapping("/curso/{cursoId}/disciplinas")
    public ResponseEntity<Object> postDisciplinaListInCurso(@PathVariable("cursoId") Long cursoId, @RequestBody List<Disciplina> disciplinas) {
        try {
            cursoService.saveDisciplinaListInCurso(cursoId, disciplinas);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch (InvalidDisciplinaException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Insere um pré-requisito em um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @param dependencia indica qual Disciplina é pré-requisito da outra.
     */
    @PostMapping("/curso/{cursoId}/preRequisito")
    public ResponseEntity<Object> postDependencia(@PathVariable("cursoId") Long cursoId, @RequestBody Dependencia dependencia) {
        try {
            cursoService.addDependencia(cursoId, dependencia.getDisciplinaA(), dependencia.getDisciplinaB());

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        catch (InvalidDependenciaException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Insere uma lista de pré-requisitos em um Curso.  
     * Função muito útil para a inserção mais rápida dos pré-requisitos de um curso.
     * 
     * @param cursoId id do Curso.
     * @param dependencias lista de dependencias.
     */
    @PostMapping("curso/{cursoId}/preRequisitos")
    public ResponseEntity<Object> postDependenciaListInCurso(@PathVariable("cursoId") Long cursoId, @RequestBody List<Dependencia> dependencias) {
        try {
            cursoService.addDependenciaListInCurso(cursoId, dependencias);

            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        catch (InvalidDependenciaException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Acha todas as disciplinas que podem ser encontradas a partir de dois dfs's (no grafo normal e transposto) 
     * com aquela disciplina sendo o ponto inicial.
     * 
     * @param cId id do Curso.
     * @param dId id da Disciplina.
     * @return conjunto de pares de disciplinas. Indicam todos os caminhos pelos quais os dfs's andaram.
     */
    @GetMapping("curso/{cursoId}/path/{disciplinaId}")
    public ResponseEntity<Object> getPathFromDisciplina(@PathVariable("cursoId") Long cId, @PathVariable("disciplinaId") Long dId) {
        try {
            return ResponseEntity.ok(cursoService.getPathFromDisciplina(cId, dId));
        }
        catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    
}
