package br.edu.infnet.logistica.servico;

import br.edu.infnet.logistica.dominio.Entrega;

public class PromocaoService {
    private static final double PESO_MINIMO_DESCONTO_KG = 10.0;
    private static final double DESCONTO_PESO_KG = 1.0;

    public Entrega aplicarPromocaoPesoPesado(Entrega entrega) {
        if (entrega.getPesoEmKg() > PESO_MINIMO_DESCONTO_KG) {
            return entrega.aplicarDescontoPeso(DESCONTO_PESO_KG);
        }
        return entrega;
    }

    public boolean isFreteGratis(Entrega entrega) {
        return entrega.isElegivelFreteGratis();
    }
}
