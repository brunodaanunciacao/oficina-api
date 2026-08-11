package br.com.fiap.oficina.interfaces.dtos;

import java.math.BigDecimal;

public record PecaResponseDTO(
        Long id,
        String codigo,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidadeEstoque
) {
}