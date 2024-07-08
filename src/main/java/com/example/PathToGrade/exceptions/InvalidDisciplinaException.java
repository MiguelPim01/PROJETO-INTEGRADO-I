package com.example.PathToGrade.exceptions;

/**
 * <i>Documentação da classe InvalidDisciplinaException</i>
 * 
 * @author Miguel Vieira Machado Pim
 * @see com.example.PathToGrade.exceptions.InvalidDisciplinaException
 * 
 * Exceção usada quando se verifica que uma Disciplina é inválida.
 */
public class InvalidDisciplinaException extends Exception {

    /**
     * Construtor.
     * 
     * @param message mensagem de erro.
     */
    public InvalidDisciplinaException(String message) {
        super(message);
    }
}
