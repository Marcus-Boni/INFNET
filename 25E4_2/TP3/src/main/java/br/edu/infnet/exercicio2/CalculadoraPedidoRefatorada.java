package br.edu.infnet.exercicio2;

import br.edu.infnet.exercicio1.ItemPedido;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class CalculadoraPedidoRefatorada {

    public ResultadoCalculo calcularTotal(List<ItemPedido> itens, Desconto desconto) {
        Objects.requireNonNull(itens, "Lista de itens não pode ser nula");
        
        BigDecimal subtotal = itens.stream()
                .map(ItemPedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (desconto == null) {
            return new ResultadoCalculo(subtotal, BigDecimal.ZERO, subtotal, null);
        }
        
        BigDecimal valorDesconto = calcularValorDesconto(subtotal, desconto);
        BigDecimal total = subtotal.subtract(valorDesconto);
        
        return new ResultadoCalculo(subtotal, valorDesconto, total, desconto);
    }

    private BigDecimal calcularValorDesconto(BigDecimal subtotal, Desconto desconto) {
        return subtotal
                .multiply(desconto.getPercentual())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
