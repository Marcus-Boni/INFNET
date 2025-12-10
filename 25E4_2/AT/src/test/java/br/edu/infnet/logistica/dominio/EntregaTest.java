package br.edu.infnet.logistica.dominio;

import br.edu.infnet.logistica.excecao.EntregaInvalidaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntregaTest {

    @Test
    void deveCriarEntregaValida() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 5.0, TipoFrete.PADRAO);

        assertEquals("João Silva", entrega.getDestinatario());
        assertEquals("Rua A, 123", entrega.getEndereco());
        assertEquals(5.0, entrega.getPesoEmKg());
        assertEquals(TipoFrete.PADRAO, entrega.getTipoFrete());
    }

    @Test
    void deveRejeitarDestinatarioNulo() {
        assertThrows(EntregaInvalidaException.class, () ->
                new Entrega(null, "Rua A, 123", 5.0, TipoFrete.PADRAO));
    }

    @Test
    void deveRejeitarDestinatarioVazio() {
        assertThrows(EntregaInvalidaException.class, () ->
                new Entrega("   ", "Rua A, 123", 5.0, TipoFrete.PADRAO));
    }

    @Test
    void deveRejeitarEnderecoNulo() {
        assertThrows(EntregaInvalidaException.class, () ->
                new Entrega("João Silva", null, 5.0, TipoFrete.PADRAO));
    }

    @Test
    void deveRejeitarEnderecoVazio() {
        assertThrows(EntregaInvalidaException.class, () ->
                new Entrega("João Silva", "", 5.0, TipoFrete.PADRAO));
    }

    @Test
    void deveRejeitarPesoNegativo() {
        assertThrows(EntregaInvalidaException.class, () ->
                new Entrega("João Silva", "Rua A, 123", -1.0, TipoFrete.PADRAO));
    }

    @Test
    void deveRejeitarPesoZero() {
        assertThrows(EntregaInvalidaException.class, () ->
                new Entrega("João Silva", "Rua A, 123", 0.0, TipoFrete.PADRAO));
    }

    @Test
    void deveRejeitarTipoFreteNulo() {
        assertThrows(EntregaInvalidaException.class, () ->
                new Entrega("João Silva", "Rua A, 123", 5.0, null));
    }

    @Test
    void deveIdentificarFreteGratisParaFreteEconomicoLeve() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 1.5, TipoFrete.ECONOMICO);
        assertTrue(entrega.isElegivelFreteGratis());
    }

    @Test
    void naoDeveSerFreteGratisParaFreteEconomicoPesado() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 3.0, TipoFrete.ECONOMICO);
        assertFalse(entrega.isElegivelFreteGratis());
    }

    @Test
    void naoDeveSerFreteGratisParaOutrosTiposDeFrete() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 1.5, TipoFrete.EXPRESSO);
        assertFalse(entrega.isElegivelFreteGratis());
    }

    @Test
    void deveAplicarDescontoPeso() {
        Entrega entregaOriginal = new Entrega("João Silva", "Rua A, 123", 11.0, TipoFrete.PADRAO);
        Entrega entregaComDesconto = entregaOriginal.aplicarDescontoPeso(1.0);

        assertEquals(10.0, entregaComDesconto.getPesoEmKg());
        assertEquals(11.0, entregaOriginal.getPesoEmKg());
    }

    @Test
    void deveManterPesoMinimoAoAplicarDesconto() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 0.5, TipoFrete.ECONOMICO);
        Entrega entregaComDesconto = entrega.aplicarDescontoPeso(1.0);

        assertEquals(0.1, entregaComDesconto.getPesoEmKg());
    }

    @Test
    void deveRejeitarDescontoNegativo() {
        Entrega entrega = new Entrega("João Silva", "Rua A, 123", 5.0, TipoFrete.PADRAO);
        assertThrows(EntregaInvalidaException.class, () -> entrega.aplicarDescontoPeso(-1.0));
    }
}
