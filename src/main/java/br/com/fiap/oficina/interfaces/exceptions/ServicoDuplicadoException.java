package br.com.fiap.oficina.interfaces.exceptions;

public class ServicoDuplicadoException extends RuntimeException {

    public ServicoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}