package br.com.fiap.oficina.interfaces.controllers;

import br.com.fiap.oficina.application.services.ServicoService;
import br.com.fiap.oficina.interfaces.dtos.ServicoRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.ServicoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> criar(
            @Valid @RequestBody ServicoRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicoService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                servicoService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                servicoService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicoRequestDTO request) {

        return ResponseEntity.ok(
                servicoService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        servicoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}