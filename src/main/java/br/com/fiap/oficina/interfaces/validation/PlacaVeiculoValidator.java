package br.com.fiap.oficina.interfaces.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PlacaVeiculoValidator implements ConstraintValidator<PlacaVeiculo, String> {

    // Aceita formato tradicional (ABC-1234 ou ABC1234) e Mercosul (ABC1D23 ou ABC-1D23)
    private static final Pattern PLACA_PATTERN = Pattern.compile(
            "^[A-Z]{3}-?[0-9][A-Z0-9][0-9]{2}$",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Deixa o @NotBlank cuidar de valores nulos/vazios
        }

        return PLACA_PATTERN.matcher(value.trim()).matches();
    }
}
