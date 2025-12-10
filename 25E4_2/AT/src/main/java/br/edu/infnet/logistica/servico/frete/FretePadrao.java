package br.edu.infnet.logistica.servico.frete;

import br.edu.infnet.logistica.dominio.Entrega;

public class FretePadrao implements CalculadoraFrete {
    private static final double MULTIPLICADOR_PESO = 1.2;

    @Override
    public double calcular(Entrega entrega) {
        return entrega.getPesoEmKg() * MULTIPLICADOR_PESO;
    }
}
