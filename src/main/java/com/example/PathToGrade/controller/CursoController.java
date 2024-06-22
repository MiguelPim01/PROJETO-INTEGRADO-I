package com.example.PathToGrade.controller;

import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;
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
    public Iterable<Curso> getCursos() {
        return cursoService.getCursos();
    }

    /**
     * Insere um Curso no banco de dados.
     * 
     * @param curso Curso passado no json.
     */
    @PostMapping("/curso")
    public void postCurso(@RequestBody Curso curso) {
        cursoService.saveCurso(curso);
    }

    /**
     * Modifica um Curso no banco de dados.
     * 
     * @param cursoId id do Curso.
     * @param curso novo Curso contendo as informações a serem modificadas.
     */
    @PutMapping("/curso/{cursoId}")
    public void putCurso(@PathVariable("cursoId") Long cursoId, @RequestBody Curso curso) {
        cursoService.modifyCurso(cursoId, curso);
    }

    /**
     * Deleta um Curso do banco de dados.
     * 
     * @param cursoId id do Curso.
     */
    @DeleteMapping("/curso/{cursoId}")
    public void deleteCurso(@PathVariable(name = "cursoId") Long cursoId) {
        cursoService.deleteCurso(cursoId);
    }

    /**
     * Retorna todas as disciplinas de um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @return Lista de Disciplinas.
     */
    @GetMapping("/curso/{cursoId}/disciplina")
    public List<Disciplina> getDisciplinasFromCurso(@PathVariable("cursoId") Long cursoId) {
        return cursoService.getDisciplinasFromCurso(cursoId);
    }

    /**
     * Insere uma Disciplina em um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @param disciplina Disciplina a ser inserida.
     */
    @PostMapping("/curso/{cursoId}/disciplina")
    public void postDisciplinaInCurso(@PathVariable("cursoId") Long cursoId, @RequestBody Disciplina disciplina) {
        cursoService.saveDisciplinaInCurso(cursoId, disciplina);
    }

    /**
     * Modifica uma Disciplina de um Curso específico.
     * 
     * @param cId id do Curso.
     * @param dId id da Disciplina.
     * @param disciplina nova Disciplina contendo as informações a serem modificadas.
     */
    @PutMapping("/curso/{cursoId}/disciplina/{disciplinaId}")
    public void putDisciplinaFromCurso(@PathVariable("cursoId") Long cId, @PathVariable("disciplinaId") Long dId, @RequestBody Disciplina disciplina) {
        cursoService.modifyDisciplinaFromCurso(cId, dId, disciplina);
    }

    /**
     * Deleta uma Disciplina de um Curso específico.
     * 
     * @param cId id do Curso.
     * @param dId id da Disciplina.
     */
    @DeleteMapping("/curso/{cursoId}/disciplina/{disciplinaId}")
    public void deleteDisciplinaFromCurso(@PathVariable("cursoId") Long cId, @PathVariable("disciplinaId") Long dId) {
        cursoService.deleteDisciplinaFromCurso(cId, dId);
    }

    /**
     * Insere uma lista de Disciplinas em um Curso.  
     * Função muito útil para a inserção mais rápida das disciplinas de um curso.
     * 
     * @param cursoId id do Curso.
     * @param disciplinas lista de Disciplinas a ser inserida.
     */
    @PostMapping("/curso/{cursoId}/disciplinas")
    public void postDisciplinaListInCurso(@PathVariable("cursoId") Long cursoId, @RequestBody List<Disciplina> disciplinas) {
        cursoService.saveDisciplinaListInCurso(cursoId, disciplinas);
    }

    /**
     * Insere um pré-requisito em um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @param dependencia indica qual Disciplina é pré-requisito da outra.
     */
    @PostMapping("/curso/{cursoId}/preRequisito")
    public void postDependencia(@PathVariable("cursoId") Long cursoId, @RequestBody Dependencia dependencia) {
        cursoService.addDependencia(cursoId, dependencia.getDisciplinaA(), dependencia.getDisciplinaB());
    }

    /**
     * Insere uma lista de pré-requisitos em um Curso.  
     * Função muito útil para a inserção mais rápida dos pré-requisitos de um curso.
     * 
     * @param cursoId id do Curso.
     * @param dependencias lista de dependencias.
     */
    @PostMapping("curso/{cursoId}/preRequisitos")
    public void postDependenciaListInCurso(@PathVariable("cursoId") Long cursoId, @RequestBody List<Dependencia> dependencias) {
        cursoService.addDependenciaListInCurso(cursoId, dependencias);
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
    public Set<Pair<Disciplina, Disciplina>> getPathFromDisciplina(@PathVariable("cursoId") Long cId, @PathVariable("disciplinaId") Long dId) {
        return cursoService.getPathFromDisciplina(cId, dId);
    }
}
