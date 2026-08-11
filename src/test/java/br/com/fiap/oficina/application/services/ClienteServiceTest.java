package br.com.fiap.oficina.application.services;

import br.com.fiap.oficina.domain.cliente.Cliente;
import br.com.fiap.oficina.infrastructure.repositories.ClienteRepository;
import br.com.fiap.oficina.interfaces.dtos.ClienteRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.ClienteResponseDTO;
import br.com.fiap.oficina.interfaces.exceptions.ClienteDuplicadoException;
import br.com.fiap.oficina.interfaces.exceptions.ClienteNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteRequestDTO request;

    @BeforeEach
    void setUp() {

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Maria Silva");
        cliente.setCpfCnpj("52998224725");
        cliente.setEmail("maria@email.com");
        cliente.setTelefone("11999999999");

        request = new ClienteRequestDTO(
                "Maria Silva",
                "52998224725",
                "maria@email.com",
                "11999999999"
        );
    }

    @Test
    void deveCriarClienteComSucesso() {

        when(clienteRepository.existsByCpfCnpj(request.cpfCnpj()))
                .thenReturn(false);

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        ClienteResponseDTO response =
                clienteService.criar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Maria Silva", response.nome());
        assertEquals("52998224725", response.cpfCnpj());

        verify(clienteRepository, times(1))
                .save(any(Cliente.class));
    }

    @Test
    void deveImpedirClienteDuplicado() {

        when(clienteRepository.existsByCpfCnpj(request.cpfCnpj()))
                .thenReturn(true);

        ClienteDuplicadoException exception =
                assertThrows(
                        ClienteDuplicadoException.class,
                        () -> clienteService.criar(request)
                );

        assertEquals(
                "Cliente já cadastrado com este CPF/CNPJ",
                exception.getMessage()
        );

        verify(clienteRepository, never())
                .save(any());
    }

    @Test
    void deveBuscarClientePorId() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        ClienteResponseDTO response =
                clienteService.buscarPorId(1L);

        assertEquals(1L, response.id());
        assertEquals("Maria Silva", response.nome());
    }

    @Test
    void deveRetornarErroQuandoClienteNaoExiste() {

        when(clienteRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ClienteNaoEncontradoException.class,
                () -> clienteService.buscarPorId(999L)
        );
    }
}