package br.com.fiap.oficina.interfaces.controllers;

import br.com.fiap.oficina.application.services.VeiculoService;
import br.com.fiap.oficina.interfaces.dtos.VeiculoRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.VeiculoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping
    public ResponseEntity<VeiculoResponseDTO> criar(
            @Valid @RequestBody VeiculoRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(veiculoService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<VeiculoResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                veiculoService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                veiculoService.buscarPorId(id)
        );
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VeiculoResponseDTO>> buscarPorCliente(
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                veiculoService.buscarPorCliente(clienteId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VeiculoRequestDTO request) {

        return ResponseEntity.ok(
                veiculoService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        veiculoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}