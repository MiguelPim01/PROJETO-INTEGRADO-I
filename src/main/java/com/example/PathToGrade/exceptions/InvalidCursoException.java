package com.example.PathToGrade.exceptions;

/**
 * <i>Documentação da classe InvalidCursoException</i>
 * 
 * @author Miguel Vieira Machado Pim
 * @see com.example.PathToGrade.exceptions.InvalidCursoException
 * 
 * Exceção usada quando se verifica que um Curso é inválido.
 */
public class InvalidCursoException extends Exception {
    
    /**
     * Construtor.
     * 
     * @param message mensagem de erro.
     */
    public InvalidCursoException(String message) {
        super(message);
    }
    
}
