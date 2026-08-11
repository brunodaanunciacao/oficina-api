package br.com.fiap.oficina.interfaces.dtos;

import java.math.BigDecimal;

public record OrdemServicoServicoResponseDTO(
        Long servicoId,
        String nome,
        BigDecimal preco
) {
}