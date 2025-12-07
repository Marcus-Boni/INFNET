package br.edu.infnet.exercicio2;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public final class ResultadoCalculo {
    private final BigDecimal subtotal;
    private final BigDecimal valorDesconto;
    private final BigDecimal total;
    private final Desconto descontoAplicado;

    public ResultadoCalculo(BigDecimal subtotal, BigDecimal valorDesconto, 
                           BigDecimal total, Desconto descontoAplicado) {
        this.subtotal = Objects.requireNonNull(subtotal, "Subtotal não pode ser nulo");
        this.valorDesconto = Objects.requireNonNull(valorDesconto, "Valor de desconto não pode ser nulo");
        this.total = Objects.requireNonNull(total, "Total não pode ser nulo");
        this.descontoAplicado = descontoAplicado;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Optional<Desconto> getDescontoAplicado() {
        return Optional.ofNullable(descontoAplicado);
    }

    public boolean temDesconto() {
        return descontoAplicado != null;
    }

    @Override
    public String toString() {
        return "ResultadoCalculo{" +
                "subtotal=" + subtotal +
                ", valorDesconto=" + valorDesconto +
                ", total=" + total +
                ", descontoAplicado=" + (descontoAplicado != null ? descontoAplicado.getCodigo() : "Nenhum") +
                '}';
    }
}
