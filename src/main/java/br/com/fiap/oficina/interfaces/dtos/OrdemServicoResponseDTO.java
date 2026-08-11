package br.com.fiap.oficina.interfaces.dtos;

import br.com.fiap.oficina.domain.ordemservico.StatusOrdemServico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdemServicoResponseDTO(

        Long id,
        Long veiculoId,
        String placaVeiculo,
        Long clienteId,
        String clienteNome,
        String descricaoProblema,
        StatusOrdemServico status,

        List<OrdemServicoServicoResponseDTO> servicos,

        List<OrdemServicoPecaResponseDTO> pecas,

        BigDecimal valorTotal,
        LocalDateTime dataAbertura

) {
}