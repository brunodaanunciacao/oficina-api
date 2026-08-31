package br.com.fiap.oficina.interfaces.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrdemServicoRequestDTO(

        @NotNull(message = "Veículo é obrigatório")
        Long veiculoId,

        @NotBlank(message = "Descrição do problema é obrigatória")
        @Size(max = 500, message = "Descrição do problema deve ter no máximo 500 caracteres")
        String descricaoProblema

) {
}