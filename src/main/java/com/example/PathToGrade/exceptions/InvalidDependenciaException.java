package com.example.PathToGrade.exceptions;

/**
 * <i>Documentação da classe InvalidDependenciaException</i>
 * 
 * @author Miguel Vieira Machado Pim
 * @see com.example.PathToGrade.exceptions.InvalidDependenciaException
 * 
 * Exceção usada quando se verifica que uma Dependencia é inválida.
 */
public class InvalidDependenciaException extends Exception {
    
    /**
     * Construtor.
     * 
     * @param message mensagem de erro.
     */
    public InvalidDependenciaException(String message) {
        super(message);
    }
}
