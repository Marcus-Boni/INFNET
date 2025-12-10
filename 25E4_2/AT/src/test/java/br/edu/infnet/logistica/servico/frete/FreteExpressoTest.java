package br.edu.infnet.logistica.servico.frete;

import br.edu.infnet.logistica.dominio.Entrega;
import br.edu.infnet.logistica.dominio.TipoFrete;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FreteExpressoTest {

    @Test
    void deveCalcularFreteExpressoCorretamente() {
        CalculadoraFrete calculadora = new FreteExpresso();
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 10.0, TipoFrete.EXPRESSO);

        double valorFrete = calculadora.calcular(entrega);

        assertEquals(25.0, valorFrete, 0.01);
    }

    @Test
    void deveCalcularFreteExpressoParaPesoMinimo() {
        CalculadoraFrete calculadora = new FreteExpresso();
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 1.0, TipoFrete.EXPRESSO);

        double valorFrete = calculadora.calcular(entrega);

        assertEquals(11.5, valorFrete, 0.01);
    }
}
