package br.edu.infnet.logistica.servico.frete;

import br.edu.infnet.logistica.dominio.Entrega;
import br.edu.infnet.logistica.dominio.TipoFrete;

import java.util.EnumMap;
import java.util.Map;

public class FabricaCalculadoraFrete {
    private final Map<TipoFrete, CalculadoraFrete> calculadoras;

    public FabricaCalculadoraFrete() {
        calculadoras = new EnumMap<>(TipoFrete.class);
        calculadoras.put(TipoFrete.EXPRESSO, new FreteExpresso());
        calculadoras.put(TipoFrete.PADRAO, new FretePadrao());
        calculadoras.put(TipoFrete.ECONOMICO, new FreteEconomico());
    }

    public CalculadoraFrete obterCalculadora(TipoFrete tipoFrete) {
        return calculadoras.get(tipoFrete);
    }

    public void registrarCalculadora(TipoFrete tipoFrete, CalculadoraFrete calculadora) {
        calculadoras.put(tipoFrete, calculadora);
    }

    public double calcularFrete(Entrega entrega) {
        CalculadoraFrete calculadora = obterCalculadora(entrega.getTipoFrete());
        return calculadora.calcular(entrega);
    }
}
