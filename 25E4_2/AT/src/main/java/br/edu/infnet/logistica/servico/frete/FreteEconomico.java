package br.edu.infnet.logistica.servico.frete;

import br.edu.infnet.logistica.dominio.Entrega;

public class FreteEconomico implements CalculadoraFrete {
    private static final double MULTIPLICADOR_PESO = 1.1;
    private static final double DESCONTO_FIXO = 5.0;

    @Override
    public double calcular(Entrega entrega) {
        double valorBase = entrega.getPesoEmKg() * MULTIPLICADOR_PESO;
        return Math.max(0, valorBase - DESCONTO_FIXO);
    }
}
