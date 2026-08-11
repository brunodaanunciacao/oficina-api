package br.com.fiap.oficina.interfaces.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ServicoRequestDTO(

        @NotBlank(message = "Nome do serviço é obrigatório")
        String nome,

        @NotBlank(message = "Descrição do serviço é obrigatória")
        String descricao,

        @NotNull(message = "Preço do serviço é obrigatório")
        @DecimalMin(
                value = "0.01",
                message = "Preço do serviço deve ser maior que zero"
        )
        BigDecimal preco

) {
}