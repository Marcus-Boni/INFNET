package br.com.infnet.calculoimc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculoIMCTest {

    @Test
    void deveClassificarComoPesoNormal() {
        CalculoIMC calculadora = new CalculoIMC();
        
        double imc = calculadora.calcularIMC(70, 1.75); 
        String classificacao = calculadora.classificarIMC(imc);
        
        assertEquals("Peso normal", classificacao);
    }
    
    @Test
    void deveClassificarComoSobrepeso() {
        CalculoIMC calculadora = new CalculoIMC();
        
        double imc = calculadora.calcularIMC(85, 1.75); 
        String classificacao = calculadora.classificarIMC(imc);
        
        assertEquals("Sobrepeso", classificacao);
    }
    
    @Test
    void deveRetornarInvalidoParaAlturaZero() {
        CalculoIMC calculadora = new CalculoIMC();
        
        double imc = calculadora.calcularIMC(70, 0); 
        String classificacao = calculadora.classificarIMC(imc);
        
        assertEquals("Inválido", classificacao);
    }
}