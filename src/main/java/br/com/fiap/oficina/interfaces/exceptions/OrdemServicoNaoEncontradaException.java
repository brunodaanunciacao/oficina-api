package br.com.fiap.oficina.interfaces.exceptions;

public class OrdemServicoNaoEncontradaException extends RuntimeException {

    public OrdemServicoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}