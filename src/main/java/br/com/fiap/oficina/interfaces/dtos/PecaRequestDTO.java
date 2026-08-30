package br.com.fiap.oficina.interfaces.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;

public record PecaRequestDTO(

        @NotBlank(message = "Código da peça é obrigatório")
        @Size(max = 50, message = "Código da peça deve ter no máximo 50 caracteres")
        String codigo,

        @NotBlank(message = "Nome da peça é obrigatório")
        @Size(max = 100, message = "Nome da peça deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "Descrição da peça é obrigatória")
        @Size(max = 255, message = "Descrição da peça deve ter no máximo 255 caracteres")
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
    public PecaRequestDTO {
        if (codigo != null) codigo = HtmlUtils.htmlEscape(codigo.trim());
        if (nome != null) nome = HtmlUtils.htmlEscape(nome.trim());
        if (descricao != null) descricao = HtmlUtils.htmlEscape(descricao.trim());
    }
}