package br.edu.infnet.logistica.servico.frete;

import br.edu.infnet.logistica.dominio.Entrega;

public class FreteExpresso implements CalculadoraFrete {
    private static final double MULTIPLICADOR_PESO = 1.5;
    private static final double TAXA_FIXA = 10.0;

    @Override
    public double calcular(Entrega entrega) {
        return entrega.getPesoEmKg() * MULTIPLICADOR_PESO + TAXA_FIXA;
    }
}
