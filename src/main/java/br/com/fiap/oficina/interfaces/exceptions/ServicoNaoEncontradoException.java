package br.com.fiap.oficina.interfaces.exceptions;

public class ServicoNaoEncontradoException extends RuntimeException {

    public ServicoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}