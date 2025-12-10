package br.edu.infnet.logistica.servico.frete;

import br.edu.infnet.logistica.dominio.Entrega;
import br.edu.infnet.logistica.dominio.TipoFrete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FabricaCalculadoraFreteTest {
    private FabricaCalculadoraFrete fabrica;

    @BeforeEach
    void setUp() {
        fabrica = new FabricaCalculadoraFrete();
    }

    @Test
    void deveRetornarCalculadoraExpresso() {
        CalculadoraFrete calculadora = fabrica.obterCalculadora(TipoFrete.EXPRESSO);
        assertInstanceOf(FreteExpresso.class, calculadora);
    }

    @Test
    void deveRetornarCalculadoraPadrao() {
        CalculadoraFrete calculadora = fabrica.obterCalculadora(TipoFrete.PADRAO);
        assertInstanceOf(FretePadrao.class, calculadora);
    }

    @Test
    void deveRetornarCalculadoraEconomico() {
        CalculadoraFrete calculadora = fabrica.obterCalculadora(TipoFrete.ECONOMICO);
        assertInstanceOf(FreteEconomico.class, calculadora);
    }

    @Test
    void deveCalcularFreteExpresso() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 10.0, TipoFrete.EXPRESSO);
        double valorFrete = fabrica.calcularFrete(entrega);
        assertEquals(25.0, valorFrete, 0.01);
    }

    @Test
    void deveCalcularFretePadrao() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 10.0, TipoFrete.PADRAO);
        double valorFrete = fabrica.calcularFrete(entrega);
        assertEquals(12.0, valorFrete, 0.01);
    }

    @Test
    void deveCalcularFreteEconomico() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 10.0, TipoFrete.ECONOMICO);
        double valorFrete = fabrica.calcularFrete(entrega);
        assertEquals(6.0, valorFrete, 0.01);
    }

    @Test
    void devePermitirRegistroDeNovaCalculadora() {
        CalculadoraFrete novaCalculadora = entrega -> 100.0;
        fabrica.registrarCalculadora(TipoFrete.EXPRESSO, novaCalculadora);

        CalculadoraFrete calculadoraRecuperada = fabrica.obterCalculadora(TipoFrete.EXPRESSO);
        assertEquals(novaCalculadora, calculadoraRecuperada);
    }
}
