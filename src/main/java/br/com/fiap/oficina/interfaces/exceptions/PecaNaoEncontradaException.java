package br.com.fiap.oficina.interfaces.exceptions;

public class PecaNaoEncontradaException extends RuntimeException {

    public PecaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}