package br.com.fiap.oficina.interfaces.exceptions;

public class VeiculoDuplicadoException extends RuntimeException {

    public VeiculoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}