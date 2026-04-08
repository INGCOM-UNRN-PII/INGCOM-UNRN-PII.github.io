package ar.unrn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ejemplo de pruebas unitarias siguiendo los estándares de la cátedra.
 * 
 * Reglas aplicadas:
 * - 0x4000: Nombre de clase termina en Test.
 * - 0x4001: Estructura Arrange-Act-Assert (AAA).
 * - 0x4002: Una sola llamada lógica por test.
 * - 0x4003: Nombres descriptivos (testMetodo_Condicion_Resultado).
 */
public class MatematicaTest {

    @Test
    void testFactorial_ConCero_RetornaUno() {
        // Arrange
        int n = 0;

        // Act
        long resultado = Matematica.factorial(n);

        // Assert
        assertEquals(1, resultado, "El factorial de 0 debe ser 1");
    }

    @Test
    void testFactorial_ConNumeroPositivo_RetornaFactorial() {
        // Arrange & Act (simplificado para casos directos)
        long resultado = Matematica.factorial(5);

        // Assert
        assertEquals(120, resultado, "El factorial de 5 debe ser 120");
    }

    @Test
    void testFactorial_ConNumeroNegativo_LanzaExcepcion() {
        // Assert & Act combinados para test de excepción
        Exception excepcion = assertThrows(IllegalArgumentException.class, () -> {
            Matematica.factorial(-1);
        });

        assertTrue(excepcion.getMessage().contains("negativo"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 4, 10, 100})
    void testEsPar_ConNumerosPares_RetornaTrue(int n) {
        assertTrue(Matematica.esPar(n));
    }

    @ParameterizedTest
    @CsvSource({
        "1, false",
        "3, false",
        "5, false"
    })
    void testEsPar_ConDiferentesValores_RetornaResultadoCorrecto(int n, boolean esperado) {
        assertEquals(esperado, Matematica.esPar(n));
    }
}
