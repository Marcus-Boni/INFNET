package br.edu.infnet.Ex01;

/**
 * Representa um item individual dentro do carrinho.
 * Esta classe é imutável e encapsula a lógica de subtotal.
 */
public class ItemCarrinho {

    // Campos são privados e finais para garantir imutabilidade e encapsulamento
    private final String nome;
    private final double precoUnitario;
    private final int quantidade;

    public ItemCarrinho(String nome, double precoUnitario, int quantidade) {
        // "Guard Clauses" garantem que um item em estado inválido não pode ser criado
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser nulo ou vazio.");
        }
        if (precoUnitario < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva.");
        }

        this.nome = nome;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    /**
     * Função pequena com propósito claro: calcular o subtotal deste item.
     * Lê como uma frase: "obter subtotal".
     */
    public double getSubtotal() {
        return this.precoUnitario * this.quantidade;
    }

    // Getters para permitir que a camada de apresentação leia os dados (ex: para imprimir um recibo)
    public String getNome() {
        return nome;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }
}