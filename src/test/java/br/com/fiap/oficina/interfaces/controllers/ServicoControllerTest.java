package br.com.fiap.oficina.interfaces.controllers;

import br.com.fiap.oficina.application.services.ServicoService;
import br.com.fiap.oficina.interfaces.dtos.ServicoRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.ServicoResponseDTO;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicoControllerTest {

    @Mock
    private ServicoService servicoService;

    @InjectMocks
    private ServicoController servicoController;

    private ServicoResponseDTO responseDTO;
    private ServicoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new ServicoRequestDTO("Alinhamento", "Alinhamento 3D", new BigDecimal("120.00"));
        responseDTO = new ServicoResponseDTO(1L, "Alinhamento", "Alinhamento 3D", new BigDecimal("120.00"));
    }

    @Test
    @DisplayName("Deve retornar 201 Created ao criar serviço")
    void deveRetornar201AoCriarServico() {
        when(servicoService.criar(any())).thenReturn(responseDTO);

        ResponseEntity<ServicoResponseDTO> response = servicoController.criar(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alinhamento", response.getBody().nome());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao listar serviços")
    void deveRetornar200AoListarServicos() {
        when(servicoService.listarTodos()).thenReturn(List.of(responseDTO));

        ResponseEntity<List<ServicoResponseDTO>> response = servicoController.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao buscar por ID")
    void deveRetornar200AoBuscarPorId() {
        when(servicoService.buscarPorId(1L)).thenReturn(responseDTO);

        ResponseEntity<ServicoResponseDTO> response = servicoController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().id());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao atualizar serviço")
    void deveRetornar200AoAtualizarServico() {
        when(servicoService.atualizar(1L, requestDTO)).thenReturn(responseDTO);

        ResponseEntity<ServicoResponseDTO> response = servicoController.atualizar(1L, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alinhamento", response.getBody().nome());
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao excluir serviço")
    void deveRetornar204AoExcluirServico() {
        ResponseEntity<Void> response = servicoController.excluir(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(servicoService).excluir(1L);
    }
}
