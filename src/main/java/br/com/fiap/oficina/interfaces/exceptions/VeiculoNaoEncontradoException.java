package br.com.fiap.oficina.interfaces.exceptions;

public class VeiculoNaoEncontradoException extends RuntimeException {

    public VeiculoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}