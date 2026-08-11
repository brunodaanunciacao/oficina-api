package br.com.fiap.oficina.application.services;

import br.com.fiap.oficina.domain.cliente.Cliente;
import br.com.fiap.oficina.domain.ordemservico.OrdemServico;
import br.com.fiap.oficina.domain.ordemservico.StatusOrdemServico;
import br.com.fiap.oficina.domain.peca.Peca;
import br.com.fiap.oficina.domain.veiculo.Veiculo;
import br.com.fiap.oficina.infrastructure.repositories.*;
import br.com.fiap.oficina.interfaces.dtos.AdicionarPecaOSDTO;
import br.com.fiap.oficina.interfaces.dtos.AtualizarStatusOSDTO;
import br.com.fiap.oficina.interfaces.dtos.OrdemServicoRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.OrdemServicoResponseDTO;
import br.com.fiap.oficina.interfaces.exceptions.EstoqueInsuficienteException;
import br.com.fiap.oficina.interfaces.exceptions.StatusOrdemServicoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {

    @Mock
    private OrdemServicoRepository ordemServicoRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private PecaRepository pecaRepository;

    @Mock
    private OrdemServicoServicoRepository ordemServicoServicoRepository;

    @Mock
    private OrdemServicoPecaRepository ordemServicoPecaRepository;

    @InjectMocks
    private OrdemServicoService ordemServicoService;

    private Cliente cliente;
    private Veiculo veiculo;
    private OrdemServico ordemServico;

    @BeforeEach
    void setUp() {

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Maria Silva");
        cliente.setCpfCnpj("52998224725");
        cliente.setEmail("maria@email.com");
        cliente.setTelefone("11999999999");

        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setPlaca("ABC1D23");
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setAno(2023);
        veiculo.setCliente(cliente);

        ordemServico = new OrdemServico();
        ordemServico.setId(1L);
        ordemServico.setVeiculo(veiculo);
        ordemServico.setDescricaoProblema(
                "Ruído ao frear"
        );
        ordemServico.setStatus(
                StatusOrdemServico.CRIADA
        );
        ordemServico.setValorTotal(
                BigDecimal.ZERO
        );
        ordemServico.setDataAbertura(
                LocalDateTime.now()
        );
    }

    @Test
    void deveCriarOrdemServico() {

        when(veiculoRepository.findById(1L))
                .thenReturn(Optional.of(veiculo));

        when(ordemServicoRepository.save(any(OrdemServico.class)))
                .thenAnswer(invocation -> {

                    OrdemServico os =
                            invocation.getArgument(0);

                    os.setId(1L);

                    return os;
                });

        when(ordemServicoServicoRepository
                .findByOrdemServicoId(1L))
                .thenReturn(List.of());

        when(ordemServicoPecaRepository
                .findByOrdemServicoId(1L))
                .thenReturn(List.of());

        OrdemServicoRequestDTO request =
                new OrdemServicoRequestDTO(
                        1L,
                        "Ruído ao frear"
                );

        OrdemServicoResponseDTO response =
                ordemServicoService.criar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(
                StatusOrdemServico.CRIADA,
                response.status()
        );
        assertEquals(
                BigDecimal.ZERO,
                response.valorTotal()
        );
    }

    @Test
    void deveImpedirPecaQuandoEstoqueInsuficiente() {

        Peca peca = new Peca();

        peca.setId(1L);
        peca.setCodigo("FLT-001");
        peca.setNome("Filtro");
        peca.setDescricao("Filtro de óleo");
        peca.setPreco(
                new BigDecimal("49.90")
        );
        peca.setQuantidadeEstoque(2);

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        when(pecaRepository.findById(1L))
                .thenReturn(Optional.of(peca));

        AdicionarPecaOSDTO request =
                new AdicionarPecaOSDTO(
                        1L,
                        10
                );

        assertThrows(
                EstoqueInsuficienteException.class,
                () -> ordemServicoService
                        .adicionarPeca(
                                1L,
                                request
                        )
        );

        verify(ordemServicoPecaRepository, never())
                .save(any());

        verify(pecaRepository, never())
                .save(any());
    }

    @Test
    void deveImpedirTransicaoInvalidaDeStatus() {

        ordemServico.setStatus(
                StatusOrdemServico.CRIADA
        );

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        AtualizarStatusOSDTO request =
                new AtualizarStatusOSDTO(
                        StatusOrdemServico.FINALIZADA
                );

        StatusOrdemServicoInvalidoException exception =
                assertThrows(
                        StatusOrdemServicoInvalidoException.class,
                        () -> ordemServicoService
                                .atualizarStatus(
                                        1L,
                                        request
                                )
                );

        assertEquals(
                "Transição de status inválida: CRIADA -> FINALIZADA",
                exception.getMessage()
        );

        verify(ordemServicoRepository, never())
                .save(any());
    }
}