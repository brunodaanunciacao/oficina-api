package br.com.fiap.oficina.interfaces.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MovimentacaoEstoqueDTO(

        @NotNull(message = "Quantidade é obrigatória")
        @Min(
                value = 1,
                message = "Quantidade deve ser maior que zero"
        )
        Integer quantidade

) {
}