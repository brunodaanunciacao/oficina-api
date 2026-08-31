package br.com.fiap.oficina.interfaces.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpfCnpjValidatorTest {

    private CpfCnpjValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CpfCnpjValidator();
    }

    @Test
    @DisplayName("Deve retornar true para valores nulos ou vazios (deixa para @NotBlank)")
    void deveRetornarTrueParaNuloOuVazio() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "52998224725",
            "111.444.777-35",
            "11144477735",
            "529.982.247-25"
    })
    @DisplayName("Deve validar CPFs válidos com ou sem formatação")
    void deveValidarCpfValido(String cpf) {
        assertTrue(validator.isValid(cpf, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "11111111111",
            "00000000000",
            "12345678900",
            "52998224724",
            "12345"
    })
    @DisplayName("Deve rejeitar CPFs inválidos ou repetidos")
    void deveRejeitarCpfInvalido(String cpf) {
        assertFalse(validator.isValid(cpf, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "11444777000161",
            "11.444.777/0001-61",
            "33000167000101"
    })
    @DisplayName("Deve validar CNPJs válidos com ou sem formatação")
    void deveValidarCnpjValido(String cnpj) {
        assertTrue(validator.isValid(cnpj, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "00000000000000",
            "11111111111111",
            "11444777000160",
            "12345678000199"
    })
    @DisplayName("Deve rejeitar CNPJs inválidos ou repetidos")
    void deveRejeitarCnpjInvalido(String cnpj) {
        assertFalse(validator.isValid(cnpj, null));
    }
}
