package br.com.fiap.oficina.interfaces.exceptions;

public class PecaDuplicadaException extends RuntimeException {

    public PecaDuplicadaException(String mensagem) {
        super(mensagem);
    }
}