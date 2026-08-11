package br.com.fiap.oficina.interfaces.dtos;

import java.math.BigDecimal;

public record OrdemServicoPecaResponseDTO(
        Long pecaId,
        String nome,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
}