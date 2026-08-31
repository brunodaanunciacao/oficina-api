package br.com.fiap.oficina.interfaces.dtos;

import br.com.fiap.oficina.interfaces.validation.CpfCnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.util.HtmlUtils;

public record ClienteRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "CPF/CNPJ é obrigatório")
        @Size(max = 20, message = "CPF ou CNPJ deve ter no máximo 20 caracteres")
        @CpfCnpj(message = "CPF ou CNPJ inválido")
        String cpfCnpj,

        @NotBlank(message = "E-mail é obrigatório")
        @Size(max = 100, message = "E-mail deve ter no máximo 100 caracteres")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone
) {
    public ClienteRequestDTO {
        if (nome != null) nome = HtmlUtils.htmlEscape(nome.trim());
        if (cpfCnpj != null) cpfCnpj = cpfCnpj.trim();
        if (email != null) email = email.trim();
        if (telefone != null) telefone = HtmlUtils.htmlEscape(telefone.trim());
    }
}