package br.edu.infnet.logistica.servico.frete;

import br.edu.infnet.logistica.dominio.Entrega;
import br.edu.infnet.logistica.dominio.TipoFrete;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FretePadraoTest {

    @Test
    void deveCalcularFretePadraoCorretamente() {
        CalculadoraFrete calculadora = new FretePadrao();
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 10.0, TipoFrete.PADRAO);

        double valorFrete = calculadora.calcular(entrega);

        assertEquals(12.0, valorFrete, 0.01);
    }

    @Test
    void deveCalcularFretePadraoParaPesoMinimo() {
        CalculadoraFrete calculadora = new FretePadrao();
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 1.0, TipoFrete.PADRAO);

        double valorFrete = calculadora.calcular(entrega);

        assertEquals(1.2, valorFrete, 0.01);
    }
}
