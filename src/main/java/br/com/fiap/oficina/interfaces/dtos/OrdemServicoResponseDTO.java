package br.com.fiap.oficina.interfaces.dtos;

import br.com.fiap.oficina.domain.ordemservico.StatusOrdemServico;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdemServicoResponseDTO(

        Long id,
        Long veiculoId,
        String placaVeiculo,
        Long clienteId,
        String clienteNome,
        String descricaoProblema,
        StatusOrdemServico status,
        BigDecimal valorTotal,
        LocalDateTime dataAbertura

) {
}