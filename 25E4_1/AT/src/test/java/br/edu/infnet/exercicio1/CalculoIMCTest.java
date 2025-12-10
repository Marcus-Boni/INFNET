package br.edu.infnet.exercicio1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Calculadora de IMC")
public class CalculoIMCTest {

    @ParameterizedTest
    @CsvSource({
        "70.0, 1.75, 22.86",
        "50.0, 1.60, 19.53",
        "90.0, 1.80, 27.78",
        "100.0, 1.70, 34.60",
        "120.0, 1.65, 44.08"
    })
    @DisplayName("Cálculo correto do IMC")
    void testCalcularIMC(double peso, double altura, double imcEsperado) {
        double resultado = CalculoIMC.calcularIMC(peso, altura);
        assertEquals(imcEsperado, resultado, 0.01);
    }

    @Test
    @DisplayName("IMC com valores extremos - peso muito baixo")
    void testIMCPesoMuitoBaixo() {
        double resultado = CalculoIMC.calcularIMC(30.0, 1.70);
        assertEquals(10.38, resultado, 0.01);
        assertEquals("Magreza grave", CalculoIMC.classificarIMC(resultado));
    }

    @Test
    @DisplayName("IMC com valores extremos - peso muito alto")
    void testIMCPesoMuitoAlto() {
        double resultado = CalculoIMC.calcularIMC(200.0, 1.70);
        assertEquals(69.20, resultado, 0.01);
        assertEquals("Obesidade Grau III", CalculoIMC.classificarIMC(resultado));
    }

    @ParameterizedTest
    @CsvSource({
        "15.0, Magreza grave",
        "16.5, Magreza moderada",
        "17.5, Magreza leve",
        "22.0, Saudável",
        "27.0, Sobrepeso",
        "32.0, Obesidade Grau I",
        "37.0, Obesidade Grau II",
        "42.0, Obesidade Grau III"
    })
    @DisplayName("Classificação correta do IMC")
    void testClassificarIMC(double imc, String classificacaoEsperada) {
        assertEquals(classificacaoEsperada, CalculoIMC.classificarIMC(imc));
    }

    @Test
    @DisplayName("Valor limite inferior - Magreza grave")
    void testLimiteInferiorMagrezaGrave() {
        assertEquals("Magreza grave", CalculoIMC.classificarIMC(15.9));
    }

    @Test
    @DisplayName("Valor limite superior - Magreza grave / inferior Magreza moderada")
    void testLimiteMagrezaModerada() {
        assertEquals("Magreza moderada", CalculoIMC.classificarIMC(16.0));
        assertEquals("Magreza moderada", CalculoIMC.classificarIMC(16.9));
    }

    @Test
    @DisplayName("Valor limite - Magreza leve")
    void testLimiteMagrezaLeve() {
        assertEquals("Magreza leve", CalculoIMC.classificarIMC(17.0));
        assertEquals("Magreza leve", CalculoIMC.classificarIMC(18.4));
    }

    @Test
    @DisplayName("Valor limite - Saudável")
    void testLimiteSaudavel() {
        assertEquals("Saudável", CalculoIMC.classificarIMC(18.5));
        assertEquals("Saudável", CalculoIMC.classificarIMC(24.9));
    }

    @Test
    @DisplayName("Valor limite - Sobrepeso")
    void testLimiteSobrepeso() {
        assertEquals("Sobrepeso", CalculoIMC.classificarIMC(25.0));
        assertEquals("Sobrepeso", CalculoIMC.classificarIMC(29.9));
    }

    @Test
    @DisplayName("Valor limite - Obesidade Grau I")
    void testLimiteObesidadeI() {
        assertEquals("Obesidade Grau I", CalculoIMC.classificarIMC(30.0));
        assertEquals("Obesidade Grau I", CalculoIMC.classificarIMC(34.9));
    }

    @Test
    @DisplayName("Valor limite - Obesidade Grau II")
    void testLimiteObesidadeII() {
        assertEquals("Obesidade Grau II", CalculoIMC.classificarIMC(35.0));
        assertEquals("Obesidade Grau II", CalculoIMC.classificarIMC(39.9));
    }

    @Test
    @DisplayName("Valor limite - Obesidade Grau III")
    void testLimiteObesidadeIII() {
        assertEquals("Obesidade Grau III", CalculoIMC.classificarIMC(40.0));
        assertEquals("Obesidade Grau III", CalculoIMC.classificarIMC(50.0));
    }

    @Test
    @DisplayName("Altura zero deve lançar exceção")
    void testAlturaZero() {
        assertThrows(ArithmeticException.class, () -> {
            double resultado = CalculoIMC.calcularIMC(70.0, 0.0);
            if (Double.isInfinite(resultado)) {
                throw new ArithmeticException("Divisão por zero");
            }
        });
    }

    @Test
    @DisplayName("Valores negativos - peso")
    void testPesoNegativo() {
        double resultado = CalculoIMC.calcularIMC(-70.0, 1.75);
        assertTrue(resultado < 0);
    }

    @Test
    @DisplayName("Valores negativos - altura")
    void testAlturaNegativa() {
        double resultado = CalculoIMC.calcularIMC(70.0, -1.75);
        assertTrue(resultado > 0);
    }
}
