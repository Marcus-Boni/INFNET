package br.edu.infnet.exercicio2;

import java.math.BigDecimal;
import java.util.Objects;

public final class Desconto {
    private final BigDecimal percentual;
    private final String codigo;
    private final String descricao;

    public Desconto(BigDecimal percentual, String codigo, String descricao) {
        this.percentual = Objects.requireNonNull(percentual, "Percentual não pode ser nulo");
        this.codigo = Objects.requireNonNull(codigo, "Código não pode ser nulo");
        this.descricao = descricao;
        
        if (percentual.compareTo(BigDecimal.ZERO) < 0 || percentual.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentual deve estar entre 0 e 100");
        }
    }

    public BigDecimal getPercentual() {
        return percentual;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Desconto desconto = (Desconto) o;
        return Objects.equals(codigo, desconto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Desconto{" +
                "percentual=" + percentual +
                ", codigo='" + codigo + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
