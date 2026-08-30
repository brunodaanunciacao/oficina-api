package br.com.fiap.oficina.interfaces.controllers;

import br.com.fiap.oficina.application.services.VeiculoService;
import br.com.fiap.oficina.interfaces.dtos.VeiculoRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.VeiculoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VeiculoControllerTest {

    @Mock
    private VeiculoService veiculoService;

    @InjectMocks
    private VeiculoController veiculoController;

    private VeiculoResponseDTO responseDTO;
    private VeiculoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new VeiculoRequestDTO("BRA2E19", "Chevrolet", "Onix", 2023, 1L);
        responseDTO = new VeiculoResponseDTO(10L, "BRA2E19", "Chevrolet", "Onix", 2023, 1L, "Carlos Andrade");
    }

    @Test
    @DisplayName("Deve retornar 201 Created ao cadastrar veículo")
    void deveRetornar201AoCriarVeiculo() {
        when(veiculoService.criar(any())).thenReturn(responseDTO);

        ResponseEntity<VeiculoResponseDTO> response = veiculoController.criar(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("BRA2E19", response.getBody().placa());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao listar veículos por cliente")
    void deveRetornar200AoBuscarPorCliente() {
        when(veiculoService.buscarPorCliente(1L)).thenReturn(List.of(responseDTO));

        ResponseEntity<List<VeiculoResponseDTO>> response = veiculoController.buscarPorCliente(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao buscar por ID")
    void deveRetornar200AoBuscarPorId() {
        when(veiculoService.buscarPorId(10L)).thenReturn(responseDTO);

        ResponseEntity<VeiculoResponseDTO> response = veiculoController.buscarPorId(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10L, response.getBody().id());
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao excluir veículo")
    void deveRetornar204AoExcluirVeiculo() {
        ResponseEntity<Void> response = veiculoController.excluir(10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(veiculoService).excluir(10L);
    }
}
