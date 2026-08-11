package br.com.fiap.oficina.interfaces.dtos;

import java.math.BigDecimal;

public record ServicoResponseDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco
) {
}