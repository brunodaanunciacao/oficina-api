package br.com.fiap.oficina.interfaces.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrdemServicoRequestDTO(

        @NotNull(message = "Veículo é obrigatório")
        Long veiculoId,

        @NotBlank(message = "Descrição do problema é obrigatória")
        String descricaoProblema

) {
}