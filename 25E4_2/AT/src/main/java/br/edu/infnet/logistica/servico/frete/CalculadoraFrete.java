package br.edu.infnet.logistica.servico.frete;

import br.edu.infnet.logistica.dominio.Entrega;

public interface CalculadoraFrete {
    double calcular(Entrega entrega);
}
