package br.edu.infnet.logistica.servico;

import br.edu.infnet.logistica.dominio.Entrega;
import br.edu.infnet.logistica.dominio.TipoFrete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromocaoServiceTest {
    private PromocaoService promocaoService;

    @BeforeEach
    void setUp() {
        promocaoService = new PromocaoService();
    }

    @Test
    void deveAplicarPromocaoParaPesoAcimaDe10kg() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 11.0, TipoFrete.PADRAO);
        Entrega entregaComPromocao = promocaoService.aplicarPromocaoPesoPesado(entrega);

        assertEquals(10.0, entregaComPromocao.getPesoEmKg());
    }

    @Test
    void naoDeveAplicarPromocaoParaPesoIgualA10kg() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 10.0, TipoFrete.PADRAO);
        Entrega entregaComPromocao = promocaoService.aplicarPromocaoPesoPesado(entrega);

        assertEquals(10.0, entregaComPromocao.getPesoEmKg());
    }

    @Test
    void naoDeveAplicarPromocaoParaPesoAbaixoDe10kg() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 5.0, TipoFrete.PADRAO);
        Entrega entregaComPromocao = promocaoService.aplicarPromocaoPesoPesado(entrega);

        assertEquals(5.0, entregaComPromocao.getPesoEmKg());
    }

    @Test
    void deveIdentificarFreteGratis() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 1.5, TipoFrete.ECONOMICO);
        assertTrue(promocaoService.isFreteGratis(entrega));
    }

    @Test
    void naoDeveSerFreteGratisParaPesoAcimaDe2kg() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 3.0, TipoFrete.ECONOMICO);
        assertFalse(promocaoService.isFreteGratis(entrega));
    }

    @Test
    void naoDeveSerFreteGratisParaOutrosTipos() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 1.5, TipoFrete.EXPRESSO);
        assertFalse(promocaoService.isFreteGratis(entrega));
    }
}
