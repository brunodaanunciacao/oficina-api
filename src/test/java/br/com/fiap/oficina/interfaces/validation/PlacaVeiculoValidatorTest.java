package br.com.fiap.oficina.interfaces.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlacaVeiculoValidatorTest {

    private PlacaVeiculoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PlacaVeiculoValidator();
    }

    @Test
    @DisplayName("Deve retornar true para valores nulos ou vazios")
    void deveRetornarTrueParaNuloOuVazio() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ABC1234",
            "ABC-1234",
            "abc1234",
            "BRA2E19",
            "BRA-2E19",
            "xyz9z99"
    })
    @DisplayName("Deve validar placas nos formatos tradicional e Mercosul")
    void deveValidarPlacasValidas(String placa) {
        assertTrue(validator.isValid(placa, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "AB1234",
            "ABCD1234",
            "123ABCD",
            "ABC-12345",
            "PLACA-INVALIDA"
    })
    @DisplayName("Deve rejeitar placas em formato inválido")
    void deveRejeitarPlacasInvalidas(String placa) {
        assertFalse(validator.isValid(placa, null));
    }
}
