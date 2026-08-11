package br.com.fiap.oficina.interfaces.exceptions;

public class ClienteDuplicadoException extends RuntimeException {

    public ClienteDuplicadoException(String mensagem) {
        super(mensagem);
    }
}