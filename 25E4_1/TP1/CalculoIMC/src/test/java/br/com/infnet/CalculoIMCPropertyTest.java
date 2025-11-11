package br.com.infnet.calculoimc;
import net.jqwik.api.*;
import net.jqwik.api.constraints.Positive; 
import net.jqwik.api.constraints.DoubleRange;
import static org.assertj.core.api.Assertions.assertThat;

class CalculoIMCPropertyTest {
    
    CalculoIMC calculadora = new CalculoIMC();

    @Property 
    void imcNuncaDeveSerNegativo(
        @ForAll @Positive double peso,   
        @ForAll @Positive double altura  
      ) {
          
        double imc = calculadora.calcularIMC(peso, altura);
        String classificacao = calculadora.classificarIMC(imc);

        assertThat(imc).isGreaterThanOrEqualTo(0); 
        assertThat(classificacao).isNotEqualTo("Inválido"); 
    }

    @Property
    void testarAlturasExtremasComPesosNormais(
        @ForAll("alturasMuitoBaixas") double alturaExtrema, 
        @ForAll @DoubleRange(min = 50.0, max = 100.0) double pesoNormal 
    ) {
        double imc = calculadora.calcularIMC(pesoNormal, alturaExtrema);

        assertThat(imc).isPositive();
        assertThat(imc).isNotEqualTo(Double.POSITIVE_INFINITY);
    }

    @Provide
    Arbitrary<Double> alturasMuitoBaixas() {
        return Arbitraries.doubles().between(0.1, 0.49);
    }

    @Provide
    Arbitrary<Double> pesosExtremos() {
        return Arbitraries.doubles().between(250.0, 500.0); 
    }

    @Example 
    void testeDeExemploParaPesoNormal() {
        double peso = 70;
        double altura = 1.75;

        double imc = calculadora.calcularIMC(peso, altura);
        String classificacao = calculadora.classificarIMC(imc);

        assertThat(imc).isEqualTo(22.857142857142858);
        assertThat(classificacao).isEqualTo("Peso normal");
    }

    @Example
    void testeDeExemploLimiteSobrepeso() {
        String classif1 = calculadora.classificarIMC(24.89);
        assertThat(classif1).isEqualTo("Peso normal");
        
        String classif2 = calculadora.classificarIMC(24.9);
        assertThat(classif2).isEqualTo("Sobrepeso");
    }
}