package br.edu.infnet.exercicio2;

import java.math.BigDecimal;

public class DescontoAplicavel {
    private BigDecimal percentual;
    private BigDecimal valorDesconto;
    private boolean aplicado;

    public DescontoAplicavel(BigDecimal percentual) {
        this.percentual = percentual;
        this.valorDesconto = BigDecimal.ZERO;
        this.aplicado = false;
    }

    public BigDecimal getPercentual() {
        return percentual;
    }

    public void setPercentual(BigDecimal percentual) {
        this.percentual = percentual;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public boolean isAplicado() {
        return aplicado;
    }

    public void setAplicado(boolean aplicado) {
        this.aplicado = aplicado;
    }
}
