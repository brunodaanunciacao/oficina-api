package br.com.fiap.oficina.interfaces.controllers;

import br.com.fiap.oficina.application.services.PecaService;
import br.com.fiap.oficina.interfaces.dtos.MovimentacaoEstoqueDTO;
import br.com.fiap.oficina.interfaces.dtos.PecaRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.PecaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    private final PecaService pecaService;

    public PecaController(PecaService pecaService) {
        this.pecaService = pecaService;
    }

    @PostMapping
    public ResponseEntity<PecaResponseDTO> criar(
            @Valid @RequestBody PecaRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pecaService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<PecaResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                pecaService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PecaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pecaService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PecaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PecaRequestDTO request) {

        return ResponseEntity.ok(
                pecaService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        pecaService.excluir(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estoque/entrada")
    public ResponseEntity<PecaResponseDTO> adicionarEstoque(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoEstoqueDTO request) {

        return ResponseEntity.ok(
                pecaService.adicionarEstoque(id, request)
        );
    }

    @PatchMapping("/{id}/estoque/baixa")
    public ResponseEntity<PecaResponseDTO> baixarEstoque(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoEstoqueDTO request) {

        return ResponseEntity.ok(
                pecaService.baixarEstoque(id, request)
        );
    }
}