package com.example.PathToGrade.service;

import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.PathToGrade.domain.Curso;
import com.example.PathToGrade.domain.Dependencia;
import com.example.PathToGrade.domain.Disciplina;
import com.example.PathToGrade.exceptions.InvalidCursoException;
import com.example.PathToGrade.exceptions.InvalidDependenciaException;
import com.example.PathToGrade.exceptions.InvalidDisciplinaException;
import com.example.PathToGrade.repository.CursoRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * <i>Documentação da classe CursoService</i>
 * @author Miguel Vieira Machado Pim
 * @see com.example.PathToGrade.service.CursoService
 * 
 * Está classe prove para a controladora uma interface de métodos para tudo que envolva o repositório de cursos.
 */
@Service
public class CursoService {
    
    /**
     * Repositório de Cursos.
     */
    @Autowired
    private CursoRepository cursoRepository;

    /**
     * Salva um Curso no repositório.
     * 
     * @param curso Curso a ser salvo.
     * @return id do curso salvo
     * @throws InvalidCursoException
     */
    public Long saveCurso(Curso curso) throws InvalidCursoException {
        Iterable<Curso> cursos = cursoRepository.findAll();

        for (Curso c : cursos) {
            if (c.getNome().equals(curso.getNome()) && c.getQtdPeriodos() == curso.getQtdPeriodos()) {
                throw new InvalidCursoException("Curso já existe");
            }
        }
        if (curso.getNome() == null || !(curso.getQtdPeriodos() > 0 && curso.getQtdPeriodos() <= 12)) {
            throw new InvalidCursoException("Curso Inválido");
        }

        cursoRepository.save(curso);

        return cursoRepository.findByNome(curso.getNome()).get().getId();
    }

    /**
     * Retorna curso para a classe controller
     * 
     * @param id id do curso.
     * @return Curso
     * @throws EntityNotFoundException
     */
    public Curso getCursoById(Long id) throws EntityNotFoundException {
        Curso curso = cursoRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com id " + id));

        return curso;
    }

    /**
     * Retorna todos os Cursos do repositório.
     * 
     * @return lista de Cursos.
     */
    public Iterable<Curso> getCursos() {
        return cursoRepository.findAll();
    }

    /**
     * Modifica um Curso específico no repositório.
     * 
     * @param cursoId id do Curso.
     * @param curso novo Curso contendo as informações a serem modificadas.
     * @throws RunTimeException
     */
    public void modifyCurso(Long cursoId, Curso curso) throws EntityNotFoundException, InvalidCursoException {
        Curso c = this.getCursoById(cursoId);

        if (curso.getNome() == null || !(curso.getQtdPeriodos() > 0 && curso.getQtdPeriodos() <= 12)) {
            throw new InvalidCursoException("Curso Inválido");
        }

        c.setNome(curso.getNome());
        c.setQtdPeriodos(curso.getQtdPeriodos());

        cursoRepository.save(c);
    }

    /**
     * Deleta um Curso do repositório.
     * 
     * @param id id do Curso.
     */
    public void deleteCurso(Long id) throws EntityNotFoundException  {
        
        if (!cursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Curso não encontrado com id " + id);
        }

        cursoRepository.deleteById(id);
    }

    /**
     * Salva uma Disciplina em um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @param disciplina Disciplina a ser inserida.
     * @throws InvalidDisciplinaException
     * @throws EntityNotFoundException
     */
    public Long saveDisciplinaInCurso(Long cursoId, Disciplina disciplina) throws InvalidDisciplinaException, EntityNotFoundException {
        Curso curso = this.getCursoById(cursoId);

        // Verifica se a disciplina é invalida
        if (disciplina.getCodigo() == null || 
            disciplina.getNome() == null || 
            !(disciplina.getPeriodo() > 0 && disciplina.getPeriodo() <= curso.getQtdPeriodos()) ||
            disciplina.getCargaHoraria() == null) {

            throw new InvalidDisciplinaException("Disciplina Inválida!");
        }

        // Verifica se a disciplina ja existe no curso
        for (Disciplina d : curso.getDisciplinas()) {
            if (d.getCodigo().equals(disciplina.getCodigo())) {
                throw new InvalidDisciplinaException("Disciplina já existe!");
            }
        }

        // Faz a relação entre a Disciplina e o Curso
        curso.addDisciplina(disciplina);

        // Leva essa informação para o banco de dados
        cursoRepository.save(curso);

        return curso.getDisciplinaByCodigo(disciplina.getCodigo()).getId();
    }

    /**
     * Método para retornar uma Disciplina dado o seu id.
     * 
     * @param cId id do curso.
     * @param dId id da disciplina.
     * @return Disciplina
     * @throws EntityNotFoundException
     */
    public Disciplina getDisciplinaById(Long cId, Long dId) throws EntityNotFoundException {
        Curso curso = this.getCursoById(cId);

        Disciplina disciplina = null;

        for (Disciplina d : curso.getDisciplinas()) {
            if (d.getId().equals(dId)) {
                disciplina = d;
            }
        }

        if (disciplina == null) {
            throw new EntityNotFoundException("Disciplina não encontrada com id " + dId);
        }
        else {
            return disciplina;
        }
    }

    /**
     * Retorna todas as disiciplinas de um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @return Lista de Disciplinas.
     * @throws RunTimeException
     */
    public List<Disciplina> getDisciplinasFromCurso(Long cursoId) throws EntityNotFoundException {
        Curso curso = this.getCursoById(cursoId);

        return curso.getDisciplinas();
    }

    /**
     * Modifica um Disciplina de um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @param disciplinaId id da Disciplina.
     * @param disciplina nova Disciplina contendo as informações a serem modificadas.
     * @throws InvalidDisciplinaException
     * @throws EntityNotFoundException
     */
    public void modifyDisciplinaFromCurso(Long cursoId, Long disciplinaId, Disciplina disciplina) throws InvalidDisciplinaException, EntityNotFoundException {
        Curso curso = this.getCursoById(cursoId);

        // Verifica se a disciplina é invalida
        if (disciplina.getCodigo() == null || 
            disciplina.getNome() == null || 
            !(disciplina.getPeriodo() > 0 && disciplina.getPeriodo() <= curso.getQtdPeriodos()) ||
            disciplina.getCargaHoraria() == null) {

            throw new InvalidDisciplinaException("Error: Disciplina Inválida!");
        }

        for (Disciplina d : curso.getDisciplinas()) {
            if (d.getId().equals(disciplinaId)) {
                d.modifyDisciplina(disciplina);
                break;
            }
        }

        cursoRepository.save(curso);
    }

    /**
     * Deleta um Disciplina de um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @param disciplinaId id da Disciplina.
     * @throws EntityNotFoundException
     */
    public void deleteDisciplinaFromCurso(Long cursoId, Long disciplinaId) throws EntityNotFoundException {
        Curso curso = this.getCursoById(cursoId);

        boolean flag = false;

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
            throw new EntityNotFoundException("Disciplina não encontrada com id: " + disciplinaId);
        }
        
    }

    /**
     * Salva uma lista de Disciplinas em um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @param disciplinas lista de Disciplinas a serem inseridas.
     * @throws InvalidDisciplinaException
     * @throws EntityNotFoundException
     */
    public void saveDisciplinaListInCurso(Long cursoId, List<Disciplina> disciplinas) throws InvalidDisciplinaException, EntityNotFoundException {
        
        for (Disciplina d : disciplinas) {
            this.saveDisciplinaInCurso(cursoId, d);    
        }
    }

    /**
     * Adiciona um pré-requisito a um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @param disciplinaA código do pré-requisito.
     * @param disciplinaB código da Disciplina.
     * @throws EntityNotFoundException
     * @throws InvalidDependenciaException
     */
    public void addDependencia(Long cursoId, String disciplinaA, String disciplinaB) throws EntityNotFoundException, InvalidDependenciaException {
        Curso curso = this.getCursoById(cursoId);

        if (disciplinaA == null || disciplinaB == null) {
            throw new InvalidDependenciaException("Dependencia Inválida");
        }

        curso.addPreRequisito(disciplinaA, disciplinaB);

        cursoRepository.save(curso);
    }

    /**
     * Adiciona um lista de pré-requisitos a um Curso específico.
     * 
     * @param cursoId id do Curso.
     * @param dependencias lista de Dependencias.
     * @throws InvalidDependenciaException
     * @throws EntityNotFoundException
     */
    public void addDependenciaListInCurso(Long cursoId, List<Dependencia> dependencias) throws InvalidDependenciaException, EntityNotFoundException {
        for (Dependencia d : dependencias) {
            this.addDependencia(cursoId, d.getDisciplinaA(), d.getDisciplinaB());
        }
    }

    /**
     * Acha todas as disciplinas que podem ser encontradas a partir de dois dfs's (no grafo normal e transposto) 
     * com aquela disciplina sendo o ponto inicial.
     * 
     * @param cId id do Curso.
     * @param dId id da Disciplina.
     * @return conjunto de pares de disciplinas. Indicam todos os caminhos pelos quais os dfs's andaram.
     * @throws EntityNotFoundException
     */
    public Set<Pair<Disciplina, Disciplina>> getPathFromDisciplina(Long cId, Long dId) throws EntityNotFoundException {
        Curso curso = this.getCursoById(cId);

        return curso.findPathToDisciplina(dId);
    }

}
