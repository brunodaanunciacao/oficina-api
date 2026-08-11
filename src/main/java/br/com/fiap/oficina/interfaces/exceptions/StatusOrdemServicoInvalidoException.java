package br.com.fiap.oficina.interfaces.exceptions;

public class StatusOrdemServicoInvalidoException extends RuntimeException {

    public StatusOrdemServicoInvalidoException(String mensagem) {
        super(mensagem);
    }
}