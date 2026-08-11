package br.com.fiap.oficina.interfaces.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarClienteNaoEncontrado(
            ClienteNaoEncontradoException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "error", "Not Found",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(ClienteDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> tratarClienteDuplicado(
            ClienteDuplicadoException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", 409,
                        "error", "Conflict",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(VeiculoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarVeiculoNaoEncontrado(
            VeiculoNaoEncontradoException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "error", "Not Found",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(VeiculoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> tratarVeiculoDuplicado(
            VeiculoDuplicadoException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", 409,
                        "error", "Conflict",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(ServicoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarServicoNaoEncontrado(
            ServicoNaoEncontradoException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "error", "Not Found",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(ServicoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> tratarServicoDuplicado(
            ServicoDuplicadoException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", 409,
                        "error", "Conflict",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(
            MethodArgumentNotValidException exception) {

        String mensagem = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(erro -> erro.getDefaultMessage())
                .orElse("Dados inválidos");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "error", "Bad Request",
                        "message", mensagem
                ));
    }
}