package br.com.fiap.oficina.interfaces.controllers;

import br.com.fiap.oficina.application.services.PecaService;
import br.com.fiap.oficina.interfaces.dtos.MovimentacaoEstoqueDTO;
import br.com.fiap.oficina.interfaces.dtos.PecaRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.PecaResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PecaControllerTest {

    @Mock
    private PecaService pecaService;

    @InjectMocks
    private PecaController pecaController;

    private PecaResponseDTO responseDTO;
    private PecaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new PecaRequestDTO("PEC01", "Filtro de Óleo", "Filtro de óleo do motor", new BigDecimal("45.00"), 10);
        responseDTO = new PecaResponseDTO(1L, "PEC01", "Filtro de Óleo", "Filtro de óleo do motor", new BigDecimal("45.00"), 10);
    }

    @Test
    @DisplayName("Deve retornar 201 Created ao cadastrar peça")
    void deveRetornar201AoCriarPeca() {
        when(pecaService.criar(any())).thenReturn(responseDTO);

        ResponseEntity<PecaResponseDTO> response = pecaController.criar(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PEC01", response.getBody().codigo());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao listar peças")
    void deveRetornar200AoListarPecas() {
        when(pecaService.listarTodos()).thenReturn(List.of(responseDTO));

        ResponseEntity<List<PecaResponseDTO>> response = pecaController.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao buscar peça por ID")
    void deveRetornar200AoBuscarPorId() {
        when(pecaService.buscarPorId(1L)).thenReturn(responseDTO);

        ResponseEntity<PecaResponseDTO> response = pecaController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().id());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao adicionar estoque")
    void deveRetornar200AoAdicionarEstoque() {
        MovimentacaoEstoqueDTO movimentacao = new MovimentacaoEstoqueDTO(5);
        when(pecaService.adicionarEstoque(eq(1L), any())).thenReturn(responseDTO);

        ResponseEntity<PecaResponseDTO> response = pecaController.adicionarEstoque(1L, movimentacao);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pecaService).adicionarEstoque(eq(1L), any());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao baixar estoque")
    void deveRetornar200AoBaixarEstoque() {
        MovimentacaoEstoqueDTO movimentacao = new MovimentacaoEstoqueDTO(2);
        when(pecaService.baixarEstoque(eq(1L), any())).thenReturn(responseDTO);

        ResponseEntity<PecaResponseDTO> response = pecaController.baixarEstoque(1L, movimentacao);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pecaService).baixarEstoque(eq(1L), any());
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao excluir peça")
    void deveRetornar204AoExcluirPeca() {
        ResponseEntity<Void> response = pecaController.excluir(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(pecaService).excluir(1L);
    }
}
