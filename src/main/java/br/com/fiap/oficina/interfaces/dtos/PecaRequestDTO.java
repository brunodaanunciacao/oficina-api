package br.com.fiap.oficina.interfaces.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PecaRequestDTO(

        @NotBlank(message = "Código da peça é obrigatório")
        String codigo,

        @NotBlank(message = "Nome da peça é obrigatório")
        String nome,

        @NotBlank(message = "Descrição da peça é obrigatória")
        String descricao,

        @NotNull(message = "Preço da peça é obrigatório")
        @DecimalMin(
                value = "0.01",
                message = "Preço da peça deve ser maior que zero"
        )
        BigDecimal preco,

        @NotNull(message = "Quantidade em estoque é obrigatória")
        @Min(
                value = 0,
                message = "Quantidade em estoque não pode ser negativa"
        )
        Integer quantidadeEstoque

) {
}