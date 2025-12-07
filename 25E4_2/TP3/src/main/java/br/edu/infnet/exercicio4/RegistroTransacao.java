package br.edu.infnet.exercicio4;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class RegistroTransacao {
    private final String identificador;
    private final BigDecimal valorMonetario;
    private final int quantidade;
    private final LocalDateTime dataCriacao;
    private final LocalDateTime dataAtualizacao;
    private final StatusTransacao status;
    private final CategoriaTransacao categoria;
    private final String descricao;

    public RegistroTransacao(BigDecimal valorMonetario, int quantidade, 
                            StatusTransacao status, CategoriaTransacao categoria, String descricao) {
        this.identificador = UUID.randomUUID().toString();
        this.valorMonetario = Objects.requireNonNull(valorMonetario, "Valor monetário não pode ser nulo");
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        this.status = Objects.requireNonNull(status, "Status não pode ser nulo");
        this.categoria = Objects.requireNonNull(categoria, "Categoria não pode ser nula");
        this.descricao = Objects.requireNonNull(descricao, "Descrição não pode ser nula");
        
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.quantidade = quantidade;
        
        if (valorMonetario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor monetário não pode ser negativo");
        }
    }

    private RegistroTransacao(String identificador, BigDecimal valorMonetario, int quantidade,
                             LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
                             StatusTransacao status, CategoriaTransacao categoria, String descricao) {
        this.identificador = identificador;
        this.valorMonetario = valorMonetario;
        this.quantidade = quantidade;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.status = status;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public RegistroTransacao atualizarStatus(StatusTransacao novoStatus) {
        Objects.requireNonNull(novoStatus, "Novo status não pode ser nulo");
        return new RegistroTransacao(
            this.identificador, this.valorMonetario, this.quantidade,
            this.dataCriacao, LocalDateTime.now(), novoStatus, this.categoria, this.descricao
        );
    }

    public RegistroTransacao atualizarCategoria(CategoriaTransacao novaCategoria) {
        Objects.requireNonNull(novaCategoria, "Nova categoria não pode ser nula");
        return new RegistroTransacao(
            this.identificador, this.valorMonetario, this.quantidade,
            this.dataCriacao, LocalDateTime.now(), this.status, novaCategoria, this.descricao
        );
    }

    public BigDecimal calcularValorTotal() {
        return valorMonetario.multiply(BigDecimal.valueOf(quantidade));
    }

    public String getIdentificador() {
        return identificador;
    }

    public BigDecimal getValorMonetario() {
        return valorMonetario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public CategoriaTransacao getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistroTransacao that = (RegistroTransacao) o;
        return Objects.equals(identificador, that.identificador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificador);
    }

    @Override
    public String toString() {
        return "RegistroTransacao{" +
                "identificador='" + identificador + '\'' +
                ", valorMonetario=" + valorMonetario +
                ", quantidade=" + quantidade +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                ", status=" + status +
                ", categoria=" + categoria +
                ", descricao='" + descricao + '\'' +
                ", valorTotal=" + calcularValorTotal() +
                '}';
    }
}
