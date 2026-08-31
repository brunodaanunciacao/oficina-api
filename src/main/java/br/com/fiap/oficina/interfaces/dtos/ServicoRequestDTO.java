package br.com.fiap.oficina.interfaces.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;

public record ServicoRequestDTO(

        @NotBlank(message = "Nome do serviço é obrigatório")
        @Size(max = 100, message = "Nome do serviço deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "Descrição do serviço é obrigatória")
        @Size(max = 255, message = "Descrição do serviço deve ter no máximo 255 caracteres")
        String descricao,

        @NotNull(message = "Preço do serviço é obrigatório")
        @DecimalMin(
                value = "0.01",
                message = "Preço do serviço deve ser maior que zero"
        )
        BigDecimal preco

) {
    public ServicoRequestDTO {
        if (nome != null) nome = HtmlUtils.htmlEscape(nome.trim());
        if (descricao != null) descricao = HtmlUtils.htmlEscape(descricao.trim());
    }
}