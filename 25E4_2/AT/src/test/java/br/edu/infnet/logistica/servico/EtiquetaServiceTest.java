package br.edu.infnet.logistica.servico;

import br.edu.infnet.logistica.dominio.Entrega;
import br.edu.infnet.logistica.dominio.TipoFrete;
import br.edu.infnet.logistica.servico.frete.FabricaCalculadoraFrete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EtiquetaServiceTest {
    private EtiquetaService etiquetaService;

    @BeforeEach
    void setUp() {
        FabricaCalculadoraFrete fabrica = new FabricaCalculadoraFrete();
        etiquetaService = new EtiquetaService(fabrica);
    }

    @Test
    void deveGerarEtiquetaCorretamente() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 10.0, TipoFrete.EXPRESSO);
        String etiqueta = etiquetaService.gerarEtiqueta(entrega);

        assertTrue(etiqueta.contains("João Silva"));
        assertTrue(etiqueta.contains("Rua A, 123"));
        assertTrue(etiqueta.contains("25,00"));
    }

    @Test
    void deveGerarResumoPedidoCorretamente() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 10.0, TipoFrete.PADRAO);
        String resumo = etiquetaService.gerarResumoPedido(entrega);

        assertTrue(resumo.contains("João Silva"));
        assertTrue(resumo.contains("PAD"));
        assertTrue(resumo.contains("12,00"));
    }

    @Test
    void deveRejeitarFabricaNula() {
        assertThrows(IllegalArgumentException.class, () -> new EtiquetaService(null));
    }

    @Test
    void deveFormatarTipoFreteExpresso() {
        Entrega entrega = new Entrega("Maria Silva", "Rua B, 456", 5.0, TipoFrete.EXPRESSO);
        String resumo = etiquetaService.gerarResumoPedido(entrega);

        assertTrue(resumo.contains("EXP"));
    }

    @Test
    void deveFormatarTipoFreteEconomico() {
        Entrega entrega = new Entrega("Maria Silva", "Rua B, 456", 5.0, TipoFrete.ECONOMICO);
        String resumo = etiquetaService.gerarResumoPedido(entrega);

        assertTrue(resumo.contains("ECO"));
    }
}
