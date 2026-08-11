package br.com.fiap.oficina.application.services;

import br.com.fiap.oficina.domain.peca.Peca;
import br.com.fiap.oficina.infrastructure.repositories.PecaRepository;
import br.com.fiap.oficina.interfaces.dtos.MovimentacaoEstoqueDTO;
import br.com.fiap.oficina.interfaces.dtos.PecaResponseDTO;
import br.com.fiap.oficina.interfaces.exceptions.EstoqueInsuficienteException;
import br.com.fiap.oficina.interfaces.exceptions.PecaNaoEncontradaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PecaServiceTest {

    @Mock
    private PecaRepository pecaRepository;

    @InjectMocks
    private PecaService pecaService;

    private Peca peca;

    @BeforeEach
    void setUp() {

        peca = new Peca();
        peca.setId(1L);
        peca.setCodigo("FLT-001");
        peca.setNome("Filtro de óleo");
        peca.setDescricao("Filtro de óleo do motor");
        peca.setPreco(new BigDecimal("49.90"));
        peca.setQuantidadeEstoque(20);
    }

    @Test
    void deveAdicionarEstoque() {

        when(pecaRepository.findById(1L))
                .thenReturn(Optional.of(peca));

        when(pecaRepository.save(peca))
                .thenReturn(peca);

        MovimentacaoEstoqueDTO request =
                new MovimentacaoEstoqueDTO(10);

        PecaResponseDTO response =
                pecaService.adicionarEstoque(
                        1L,
                        request
                );

        assertEquals(
                30,
                response.quantidadeEstoque()
        );
    }

    @Test
    void deveBaixarEstoque() {

        when(pecaRepository.findById(1L))
                .thenReturn(Optional.of(peca));

        when(pecaRepository.save(peca))
                .thenReturn(peca);

        MovimentacaoEstoqueDTO request =
                new MovimentacaoEstoqueDTO(5);

        PecaResponseDTO response =
                pecaService.baixarEstoque(
                        1L,
                        request
                );

        assertEquals(
                15,
                response.quantidadeEstoque()
        );
    }

    @Test
    void deveImpedirBaixaComEstoqueInsuficiente() {

        when(pecaRepository.findById(1L))
                .thenReturn(Optional.of(peca));

        MovimentacaoEstoqueDTO request =
                new MovimentacaoEstoqueDTO(100);

        EstoqueInsuficienteException exception =
                assertThrows(
                        EstoqueInsuficienteException.class,
                        () -> pecaService.baixarEstoque(
                                1L,
                                request
                        )
                );

        assertEquals(
                "Estoque insuficiente para realizar a baixa",
                exception.getMessage()
        );

        verify(pecaRepository, never())
                .save(any());
    }

    @Test
    void deveRetornarErroQuandoPecaNaoExiste() {

        when(pecaRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                PecaNaoEncontradaException.class,
                () -> pecaService.buscarPorId(999L)
        );
    }
}