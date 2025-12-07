package br.edu.infnet.exercicio2;

import br.edu.infnet.exercicio1.ItemPedido;
import java.math.BigDecimal;
import java.util.List;

public class CalculadoraPedidoComEfeitosColaterais {

    public BigDecimal calcularTotal(List<ItemPedido> itens, DescontoAplicavel desconto) {
        BigDecimal total = itens.stream()
                .map(ItemPedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (desconto != null) {
            desconto.setValorDesconto(total.multiply(desconto.getPercentual()).divide(BigDecimal.valueOf(100)));
            desconto.setAplicado(true);
            total = total.subtract(desconto.getValorDesconto());
        }
        
        return total;
    }
}
