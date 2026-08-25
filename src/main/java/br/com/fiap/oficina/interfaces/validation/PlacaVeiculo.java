package br.com.fiap.oficina.interfaces.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PlacaVeiculoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PlacaVeiculo {

    String message() default "Placa de veículo inválida (deve seguir o formato tradicional ABC1234 ou Mercosul ABC1D23)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
