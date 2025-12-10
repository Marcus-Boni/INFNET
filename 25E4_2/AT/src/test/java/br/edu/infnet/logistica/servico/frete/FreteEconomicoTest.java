package br.edu.infnet.logistica.servico.frete;

import br.edu.infnet.logistica.dominio.Entrega;
import br.edu.infnet.logistica.dominio.TipoFrete;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FreteEconomicoTest {

    @Test
    void deveCalcularFreteEconomicoCorretamente() {
        CalculadoraFrete calculadora = new FreteEconomico();
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 10.0, TipoFrete.ECONOMICO);

        double valorFrete = calculadora.calcular(entrega);

        assertEquals(6.0, valorFrete, 0.01);
    }

    @Test
    void deveRetornarZeroQuandoDescontoSuperaValorBase() {
        CalculadoraFrete calculadora = new FreteEconomico();
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 1.0, TipoFrete.ECONOMICO);

        double valorFrete = calculadora.calcular(entrega);

        assertEquals(0.0, valorFrete, 0.01);
    }

    @Test
    void deveCalcularFreteEconomicoParaPesoMedio() {
        CalculadoraFrete calculadora = new FreteEconomico();
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 6.0, TipoFrete.ECONOMICO);

        double valorFrete = calculadora.calcular(entrega);

        assertEquals(1.6, valorFrete, 0.01);
    }
}
