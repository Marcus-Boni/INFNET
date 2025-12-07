package br.edu.infnet.exercicio3;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class ProdutoItem {
    private final String id;
    private final String sku;
    private final String nome;
    private final int quantidade;
    private final BigDecimal precoUnitario;

    public ProdutoItem(String sku, String nome, int quantidade, BigDecimal precoUnitario) {
        this.id = UUID.randomUUID().toString();
        this.sku = Objects.requireNonNull(sku, "SKU não pode ser nulo");
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.precoUnitario = Objects.requireNonNull(precoUnitario, "Preço unitário não pode ser nulo");
        
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.quantidade = quantidade;
        
        if (precoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo");
        }
    }

    private ProdutoItem(String id, String sku, String nome, int quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.sku = sku;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public ProdutoItem atualizarQuantidade(int novaQuantidade) {
        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        return new ProdutoItem(this.id, this.sku, this.nome, novaQuantidade, this.precoUnitario);
    }

    public ProdutoItem atualizarPreco(BigDecimal novoPreco) {
        Objects.requireNonNull(novoPreco, "Preço não pode ser nulo");
        if (novoPreco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo");
        }
        return new ProdutoItem(this.id, this.sku, this.nome, this.quantidade, novoPreco);
    }

    public ProdutoItem incrementarQuantidade(int incremento) {
        if (incremento < 0) {
            throw new IllegalArgumentException("Incremento não pode ser negativo");
        }
        return new ProdutoItem(this.id, this.sku, this.nome, this.quantidade + incremento, this.precoUnitario);
    }

    public ProdutoItem decrementarQuantidade(int decremento) {
        if (decremento < 0) {
            throw new IllegalArgumentException("Decremento não pode ser negativo");
        }
        int novaQuantidade = this.quantidade - decremento;
        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("Quantidade resultante não pode ser negativa");
        }
        return new ProdutoItem(this.id, this.sku, this.nome, novaQuantidade, this.precoUnitario);
    }

    public BigDecimal calcularValorTotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public String getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoItem that = (ProdutoItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProdutoItem{" +
                "id='" + id + '\'' +
                ", sku='" + sku + '\'' +
                ", nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                ", valorTotal=" + calcularValorTotal() +
                '}';
    }
}
